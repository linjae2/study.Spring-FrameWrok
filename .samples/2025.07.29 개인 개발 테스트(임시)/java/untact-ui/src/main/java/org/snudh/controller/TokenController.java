package org.snudh.controller;

import java.time.LocalDate;

import org.snudh.domain.PatInfoVO;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/service")
@RequiredArgsConstructor
public class TokenController {

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  @GetMapping("/token/{token}/{sid}")
  public String token(
    HttpServletRequest request,
    @PathVariable(name = "token") String token,
    @PathVariable(name = "sid") Integer sid
  ) {
    ServiceInstance serviceInstance = discoveryClient.getInstances("alim").get(0);
    PatInfoVO pi = restClient.get()
       .uri(String.format("%s/api/v1/alimtalk/token/%s", serviceInstance.getUri(), token))
       .retrieve()
       .body(PatInfoVO.class);

    if (pi != null && !"".equals(pi.getPatno())) {
      System.out.println(pi.getPatno());
      HttpSession session = request.getSession();
      session.setAttribute("PATINFO", pi);
      session.setAttribute("PATNO", pi.getPatname());
    }

    // SchedDto data = SchedDto.builder()
    //   .rsvDate(LocalDate.now().plusDays(dday))
    //   .dday(dday).msgType("진료").msgData(rs1).build();

    // service.putSchedAlim(data);
    // return ResponseEntity.ok().build();

    //session.setAttribute("userId", token);
    //session.setMaxInactiveInterval(60 * 30);
    //PatInfoVO pi = tokenProvider.getAccessTokenInfo(token);
    if (sid == 1)
      return "redirect:/service/card"; else
    if (sid == 2)
      return "redirect:/service/medi/reservation";
    if (sid == 9999)
      return "redirect:/service/hwan/index";

    return "redirect:/service/card";
  }
}
