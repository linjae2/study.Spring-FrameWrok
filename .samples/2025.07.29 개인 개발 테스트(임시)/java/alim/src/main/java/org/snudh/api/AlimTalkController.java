package org.snudh.api;

import java.time.LocalDate;
import java.time.YearMonth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snudh.domain.PatInfoVO;
import org.snudh.domain.PatToken;
import org.snudh.domain.SchedDto;
import org.snudh.event.RsvAlimEvent;
import org.snudh.provider.JwtTokenProvider;
import org.snudh.service.AlimService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alimtalk")
@RequiredArgsConstructor
public class AlimTalkController {
  private static final Logger log = LoggerFactory.getLogger(AlimService.class);

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  private final JwtTokenProvider tokenProvider;
  private final AlimService service;


  @GetMapping("/send")
  public ResponseEntity<Void> getSned() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/register/medis")
  public ResponseEntity<Void> getRegister(
    @RequestParam(name ="dday", defaultValue = "1") Integer dday
  ) {
    ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);
    String rs1 = restClient.get()
      .uri(String.format("%s/api/v1/rsvdate/medis?dday=%d", serviceInstance.getUri(), dday))
      .retrieve()
      .body(String.class);

    SchedDto data = SchedDto.builder()
      .rsvDate(LocalDate.now().plusDays(dday))
      .dday(dday).msgType("진료").msgData(rs1).build();

    service.putSchedAlim(data);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/token/{token}")
  public ResponseEntity<PatInfoVO> signcheck(
    @PathVariable(name = "token") String token
  ) {
    PatInfoVO pi = null;
    PatToken pt = service.findByToken(token);
    if (pt != null) {
      pi = tokenProvider.getAccessTokenInfo(token);
      pi.setPatname(pt.getPatName());
      pi.setDeptnm(pt.getDeptnm());
      pi.setMeddrnm(pt.getAlimdrnm());
      pi.setExpired(pt.getExpired());
    }
    return ResponseEntity.ok().body(pi);
  }
}
