package org.snudh.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.snudh.domain.PatInfoVO;
import org.snudh.ocs.dto.RsvDrInfo;
import org.snudh.ocs.dto.RsvDrSchInfo;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/service/hwan")
@RequiredArgsConstructor
public class HwanController {

  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  @RequestMapping("/index")
  public String reservation(
    HttpServletRequest request,
    Model model
  ) {
    HttpSession session = request.getSession(false);
    PatInfoVO pi = null;
    if (session != null) pi = (PatInfoVO)session.getAttribute("PATINFO");
    if (pi == null) return "sessionout";

    RsvDrInfo rsvInfo = (RsvDrInfo)session.getAttribute("RSVDRINFO");;

    if (rsvInfo == null) {
      ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);
      RsvDrSchInfo rds = restClient.get()
        .uri(String.format("%s/api/v1/rsvdate/RsvDrSch/%s/%s/%s", serviceInstance.getUri()
                        , pi.getPatno()
                        , pi.getMeddate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                        , pi.getMeddept()))
        .retrieve()
        .body(RsvDrSchInfo.class);
      session.setAttribute("RSVDRINFO", rds.getRsvInfo());
      rsvInfo = rds.getRsvInfo();
    }

    String meddept = pi.getDeptnm();
    if (meddept.startsWith("원스톱협진센터")) meddept = "원스톱협진센터";

    model.addAttribute("patname", pi.getPatname());
    model.addAttribute("patno", pi.getPatno());
    model.addAttribute("meddate", pi.getMeddate());
    model.addAttribute("meddept", meddept);
    model.addAttribute("meddrnm", pi.getMeddrnm());
    return "hwan/index";
  }
}
