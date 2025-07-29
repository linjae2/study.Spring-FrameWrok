package org.snudh.api;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.snudh.domain.DrSchedule;
import org.snudh.domain.RsvInfo;
import org.snudh.service.RsvService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rsvdate/")
@RequiredArgsConstructor
public class RsvDateController {

  private final RsvService service;

  @GetMapping("/medis")
  public ResponseEntity<List<RsvInfo>> getMedis(
    @RequestParam(name ="dday", defaultValue = "1") Integer dday
  ) {
    return new ResponseEntity<>(service.getRsvMedis(dday), HttpStatus.OK);
  }

  @GetMapping("/exams")
  public ResponseEntity<List<RsvInfo>> getExams(
    @RequestParam(name ="dday", defaultValue = "1") Integer dday
  ) {
    List<RsvInfo> rs = service.getRsvExams(dday);
    return ResponseEntity.ok().body(rs);
    //return new ResponseEntity<>(service.getRsvExams(dday), HttpStatus.OK);
  }

  @GetMapping("/holydays/{ym}")
  public ResponseEntity<List<LocalDate>> getHolyDays(
    @PathVariable(name = "ym") YearMonth ym
  ) {
    List<LocalDate> rs = service.getHolyDays(ym);
    return ResponseEntity.ok().body(rs);
    //return new ResponseEntity<>(service.getRsvExams(dday), HttpStatus.OK);
  }

  @GetMapping("/holy/{ym}")
  public ResponseEntity<YearMonth> getHoly(
    @PathVariable(name = "ym") YearMonth ym
  ) {

    //YearMonth ym = YearMonth.of(2024, 9);
    //List<LocalDate> rs = service.getHolyDays();
    return ResponseEntity.ok().body(ym);
    //return new ResponseEntity<>(service.getRsvExams(dday), HttpStatus.OK);
  }

  @GetMapping("/drschedule/{meddr}/{meddept}/{ym}")
  public ResponseEntity<List<DrSchedule>> getDrSchedule(
    @PathVariable(name = "meddr"  ) String meddr,
    @PathVariable(name = "meddept") String meddept,
    @PathVariable(name = "ym"     ) YearMonth ym
  ) {
    List<DrSchedule> rs = service.getDrSchedule(meddr, meddept, ym);
    return ResponseEntity.ok().body(rs);
    //return new ResponseEntity<>(service.getRsvExams(dday), HttpStatus.OK);
  }
}
