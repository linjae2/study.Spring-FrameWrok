package org.snudh.controller.ocs;

import java.util.List;

import org.snudh.ocs.dto.RsvInfoDto;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/service/ocs")
@RequiredArgsConstructor
public class MediListController {
  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  @RequestMapping("/MediList")
  public String getMediList(
    @RequestParam(name ="dday", defaultValue = "1") Integer dday,
    Model model
  ) {
    ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);
    List<RsvInfoDto> rss = restClient.get()
      .uri(String.format("%s/api/v1/rsvdate/medis?dday=%d", serviceInstance.getUri(), dday))
      .retrieve()
      .body(new ParameterizedTypeReference<List<RsvInfoDto>>() {});

    model.addAttribute("books", rss);
    // PatInfoVO pi = null;
    // if (session != null) pi = (PatInfoVO)session.getAttribute("PATINFO");

    // if (pi != null) {
    //   String txtno = pi.getPatno();
    //   txtno = txtno.substring(0, 2) + ' ' + txtno.substring(2, 4) + ' ' + txtno.substring(4);
    //   model.addAttribute("txtno", txtno);
    //   model.addAttribute("patname", pi.getPatname());
    //   model.addAttribute("patno", pi.getPatno());
    // }
    return "ocs/MediList";
  }
}
