package org.snudh.event;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.AlimTalk;
import org.snudh.domain.AlimTalkRepository;
import org.snudh.domain.RsvInfoDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AlimTalkEventListener {
  private static final Logger log = LoggerFactory.getLogger(RsvAlimEventListener.class);

  private final AlimTalkRepository alimTalkRepository;

  @Async
  @EventListener
  public void sendAlimTalk(AlimTalkEvent event) throws InterruptedException {
    log.info("======================== sendAlimTalk EVENT ====================");

    if (alimTalkRepository.existsPopedData()) {
      Gson gson = new Gson();

      List<AlimTalk> at = alimTalkRepository.getPopData();
      at.stream().forEach(item -> {
        System.out.println("=>  " + item.getRefKey());

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("sender_key", "26dc6c15158be6a70a4363dea7733e43cb833f25");
        jsonObject.addProperty("nation_phone_number", item.getNationPhoneNumber());
        jsonObject.addProperty("template_code", item.getTmplCode());
        jsonObject.addProperty("message", item.getMessage());

        jsonObject.addProperty("tran_type", item.getTranType());
        jsonObject.addProperty("tran_message", item.getTranMessage());
        jsonObject.addProperty("subject", item.getSubject());
        jsonObject.addProperty("callback_number", item.getCallbackNumber());

        // JsonObject를 Json 문자열로 변환(직렬화)
        String jsonStr = gson.toJson(jsonObject);
        System.out.println(jsonStr);
      });
    }
  }

}
