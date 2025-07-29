package org.snudh.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.AlimTalk;
import org.snudh.domain.PatToken;
import org.snudh.domain.PatTokenRepository;
import org.snudh.domain.RsvAlim;
import org.snudh.domain.RsvInfoDto;
import org.snudh.domain.SchedAlim;
import org.snudh.service.AlimService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RsvAlimEventListener {
  private static final Logger log = LoggerFactory.getLogger(RsvAlimEventListener.class);

  private final ApplicationEventPublisher eventPublisher;

  private final PatTokenRepository patTokenRepository;
  private final AlimService service;

  @Async
  @EventListener
  public void DestributeRsvAlim(RsvAlimEvent event) throws Exception {
    log.info("======================== Destribute EVENT ====================");

    if (service.existsAlimPopedData()) {
      SchedAlim alim = service.topData();

      SchedAlim.Key key = alim.getKey();
      service.updateSchedAlim(key, 2);
      ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
      try {
        // System.out.println(alim.getMsgData());
        List<RsvInfoDto> rsvInfo = objectMapper.readValue(alim.getMsgData(), new TypeReference<>(){});
        rsvInfo.stream().forEach(item -> {
          RsvAlim ra = service.putRsvAlim(key, item);
          if ('N' != item.getSendyn()) {
            if (ra != null) {
              AlimTalk alimTalk = AlimTalk.of(item);
              PatToken tocken = patTokenRepository.getTockenByRsvInfo(item.getPatno(), item.getRsvdate(), item.getMeddept());

              if (tocken == null) {
                tocken = service.generatorJwtV1(item);
              }

              String RefKey = String.format("%s-%02d-%s-%02d",
                            ra.getKey().getRsvDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                            ra.getKey().getDDay(), ra.getKey().getMsgType(), ra.getKey().getSeq());

              // 당일 10시 발송 예정 등록
              alimTalk.setSendDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0,0)));
              alimTalk.setRefKey(RefKey);

              log.info(key.getMsgType());
              if ("진료".equals(key.getMsgType())) {
                service.putMediRsvTalk(item, alimTalk);
              }
            }
          }
        });
        service.updateSchedAlim(key, 0);
        //eventPublisher.publishEvent(new AlimTalkEvent());
      } catch(Exception e) {
        throw e;
      }
    }
  }
}
