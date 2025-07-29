package org.snudh.service.api.v1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.snudh.domain.PatInfoVO;
import org.snudh.ocs.dto.DrMonthSch;
import org.snudh.ocs.dto.RsvDrInfo;
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
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/service/api/v1")
@RequiredArgsConstructor
public class RsvDrSchController {
  private final RestClient restClient;
  private final DiscoveryClient discoveryClient;

  @GetMapping(value = {"/RsvDrSch", "/RsvDrSch/{patno}/{meddate}/{medept}"})
  public ResponseEntity<RsvDrSchInfo> getRsvDrSch(
    @PathVariable(name = "patno"   , required = false) String patno,
    @PathVariable(name = "meddate" , required = false) LocalDateTime meddate,
    @PathVariable(name = "medept"  , required = false) String medept,
    HttpServletRequest request
  ) {
    HttpSession session = request.getSession(false);
    PatInfoVO pi = null;
    RsvDrSchInfo rds = null;
    if (session != null) pi = (PatInfoVO)session.getAttribute("PATINFO");

    if (patno != null) {
      pi = new PatInfoVO();
      pi.setPatno(patno);
      pi.setMeddate(meddate);
      pi.setMeddept(medept);
      pi.setPatname("테스트");
      if (session == null) session = request.getSession();
      session.setAttribute("PATINFO", pi);
      session.setAttribute("PATNO", pi.getPatname());
    }

    if (pi != null) {
      ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);
      rds = restClient.get()
        .uri(String.format("%s/api/v1/rsvdate/RsvDrSch/%s/%s/%s", serviceInstance.getUri()
                        , pi.getPatno()
                        , pi.getMeddate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                        , pi.getMeddept()))
        .retrieve()
        .body(RsvDrSchInfo.class);
    }

    if (rds != null) {
      if (null != session) {
        session.setAttribute("RSVDRINFO", rds.getRsvInfo());
      }

      try {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(rds));
      } catch (Exception e)
      { }
    }

    return ResponseEntity.ok().body(rds);
  }

  @GetMapping(value = {"/RsvDrSchTimes", "/RsvDrSchTimes/{meddate}"})
  public ResponseEntity<List<LocalTime>> getRsvDrSchTimes(
    @PathVariable(name = "meddate" , required = false) LocalDate meddate,
    HttpServletRequest request
  ) {
    HttpSession session = request.getSession(false);
    PatInfoVO pi = null;
    RsvDrInfo rsvInfo = null;
    List<LocalTime> rs = null;
    if (session != null) {
      pi = (PatInfoVO)session.getAttribute("PATINFO");
      rsvInfo = (RsvDrInfo)session.getAttribute("RSVDRINFO");
    }

    if (pi == null || rsvInfo == null)
    {
      //throw new Exception();
      //throw new Exception("adfads");
    }

    ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);
    String uri = String.format("%s/api/v1/rsvdate/RsvDrSchDay/%s/%s/%s/%d/%c"
                      , serviceInstance.getUri()
                      , rsvInfo.getRsvdr()
                      , meddate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                      , pi.getMeddept()
                      , rsvInfo.getTx_time()
                      , rsvInfo.getOverlap());
    rs = restClient.get()
        .uri(uri)
        .retrieve()
        .body(new ParameterizedTypeReference<List<LocalTime>>() {});
    return ResponseEntity.ok().body(rs);
  }


  @GetMapping("/RsvDrSchMonth/{medYM}")
  public ResponseEntity<DrMonthSch> getRsvDrSchMonth(
    @PathVariable(name = "medYM"  ) YearMonth medYM,
    HttpServletRequest request
  ) {
    DrMonthSch rs = null;
    HttpSession session = request.getSession(false);
    PatInfoVO pi = null;
    RsvDrInfo rds = null;
    if (session != null) pi = (PatInfoVO)session.getAttribute("PATINFO");
    if (pi != null) rds = (RsvDrInfo)session.getAttribute("RSVDRINFO");

    if (rds != null) {
      ServiceInstance serviceInstance = discoveryClient.getInstances("ocs").get(0);
      // @GetMapping("/RsvDrSchMonth/{meddr}/{medYM}/{medept}/{tx_time}/{overlap}")
      String uri = String.format("%s/api/v1/rsvdate/RsvDrSchMonth/%s/%s/%s/%s/%s", serviceInstance.getUri()
                      , rds.getRsvdr()
                      , medYM.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                      , pi.getMeddept(), rds.getTx_time(), rds.getOverlap());

      rs = restClient.get()
        .uri(uri)
        .retrieve()
        .body(DrMonthSch.class);
    }

    return ResponseEntity.ok().body(rs);
  }
}
