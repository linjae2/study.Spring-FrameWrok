package org.snudh.controller.ocs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.snudh.ocs.dto.RsvDrSchInfo;
import org.snudh.ocs.dto.RsvInfoDto;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/service/ocs")
@RequiredArgsConstructor
public class VRsvDrSchController {
  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  @GetMapping("/RsvDrSch/{patno}/{meddate}/{medept}")
  public String getRsvDrSch(
    @PathVariable(name = "patno"  ) String patno,
    @PathVariable(name = "meddate") LocalDateTime meddate,
    @PathVariable(name = "medept" ) String medept,
    Model model
  ) {
    ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);

    model.addAttribute("patno", patno);
    model.addAttribute("meddate", meddate);
    model.addAttribute("medept", medept);
    RsvDrSchInfo rds = restClient.get()
        .uri(String.format("%s/api/v1/rsvdate/RsvDrSch/%s/%s/%s", serviceInstance.getUri()
                        , patno, meddate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), medept))
        .retrieve()
        .body(RsvDrSchInfo.class);

    if (rds != null) {
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(rds));
      } catch (Exception e)
      { }
      model.addAttribute("rsvInfo", rds.getRsvInfo());
      model.addAttribute("medYM", rds.getMedYM());
      model.addAttribute("days", rds.getDays());
    }

    return "ocs/RsvDrSch";
  }

  @GetMapping("/RsvDrSch.01/{patno}/{meddate}/{medept}")
  public String getRsvDrSch_01(
    @PathVariable(name = "patno"  ) String patno,
    @PathVariable(name = "meddate") LocalDateTime meddate,
    @PathVariable(name = "medept" ) String medept,
    Model model
  ) {
    //ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);
    model.addAttribute("patno", patno);
    model.addAttribute("meddate", meddate);
    model.addAttribute("medept", medept);
    // RsvDrSchInfo rds = restClient.get()
    //     .uri(String.format("%s/api/v1/rsvdate/RsvDrSch/%s/%s/%s", serviceInstance.getUri()
    //                     , patno, meddate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), medept))
    //     .retrieve()
    //     .body(RsvDrSchInfo.class);

    // if (rds != null) {
    //   try {
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     System.out.println(objectMapper.writeValueAsString(rds));
    //   } catch (Exception e)
    //   { }
    //   model.addAttribute("rsvInfo", rds.getRsvInfo());
    //   model.addAttribute("medYM", rds.getMedYM());
    //   model.addAttribute("days", rds.getDays());
    // }

    return "ocs/RsvDrSch.01";
  }
}
