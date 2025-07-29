package org.snudh.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.snudh.domain.DrMonthSch;
import org.snudh.domain.DrRsvInfo;
import org.snudh.domain.DrSchedule;
import org.snudh.domain.RsvDrInfo;
import org.snudh.domain.RsvDrSchInfo;
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
    List<RsvInfo> rs = service.getRsvMedis(dday);
    return new ResponseEntity<>(rs, HttpStatus.OK);
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

  // @GetMapping(value = {"/schedule/{meddr}/{meddept}", "/schedule/{meddr}/{meddept}/{ym}"})
  // public ResponseEntity<int[]> getSchedule(
  //   @PathVariable(name = "meddr"  ) String meddr,
  //   @PathVariable(name = "meddept") String meddept,
  //   @PathVariable(name = "ym"     ) Optional<YearMonth> ym
  // ) {
  //   int[] rs = service.getSchedule(meddr, meddept, ym);
  //   return ResponseEntity.ok().body(rs);
  // }

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

  @GetMapping("/drRsvInfos/{meddr}/{meddept}/{ym}")
  public ResponseEntity<List<DrRsvInfo>> getDrRsvInfos(
    @PathVariable(name = "meddr"  ) String meddr,
    @PathVariable(name = "meddept") String meddept,
    @PathVariable(name = "ym"     ) YearMonth ym
  ) {
    List<DrRsvInfo> rs = service.getDrRsvInfos(meddr, meddept, ym);
    return ResponseEntity.ok().body(rs);
    //return new ResponseEntity<>(service.getRsvExams(dday), HttpStatus.OK);
  }

  @GetMapping("/RsvInfo/{patno}/{meddate}/{medept}")
  public ResponseEntity<RsvDrInfo> getRsvInfo(
    @PathVariable(name = "patno"  ) String patno,
    @PathVariable(name = "meddate") LocalDateTime meddate,
    @PathVariable(name = "medept" ) String medept
  ) {
    List<RsvDrInfo> rs = service.getRsvInfo(patno, meddate, medept);
    //List<DrRsvInfo> rs = service.getDrRsvInfos(meddr, meddept, ym);
    //return ResponseEntity.ok().body(rs);
    //return new ResponseEntity<>(service.getRsvExams(dday), HttpStatus.OK);
    return ResponseEntity.ok().body(rs.get(0));
  }

  @GetMapping("/RsvDrSch/{patno}/{meddate}/{medept}")
  public ResponseEntity<RsvDrSchInfo> getRsvDrSch(
    @PathVariable(name = "patno"  ) String patno,
    @PathVariable(name = "meddate") LocalDateTime meddate,
    @PathVariable(name = "medept" ) String medept
  ) {
    RsvDrSchInfo rs = new RsvDrSchInfo();
    List<RsvDrInfo> rdi = service.getRsvInfo(patno, meddate, medept);
    if (!rdi.isEmpty()) {
      RsvDrInfo rd0 = rdi.get(0); rs.setRsvInfo(rd0);

      if (rd0.getChange_yn() == 'Y') {
        DrMonthSch dms = service.getSchedule(rd0.rsvdr, medept, null, rd0.tx_time, rd0.overlap);
        rs.setNextMED(dms.getNextmed());
        rs.setMedYM(dms.getMedYM());
        rs.setDays(dms.getDays());
      }
    }

    return ResponseEntity.ok().body(rs);
  }

  @GetMapping("/RsvDrSchDay/{meddr}/{meddate}/{medept}/{tx_time}/{overlap}")
  public ResponseEntity<List<LocalTime>> getRsvDrSchDay(
    @PathVariable(name = "meddr"  ) String meddr,
    @PathVariable(name = "meddate") LocalDate meddate,
    @PathVariable(name = "medept" ) String medept,
    @PathVariable(name = "tx_time" ) Integer tx_time,
    @PathVariable(name = "overlap" ) char overlap
  ) {
    List<LocalTime> rs = new ArrayList<>();
    List<Integer> rg = new ArrayList<>();
    int len = tx_time / 5;
    byte[] schedule = service.getDaySchedule(meddr, medept, meddate, tx_time, overlap);

    int sIdx = -1, eLen = 0;
    for(int idx = 0; idx < 157; ++idx) {
      if (schedule[idx] > 0) {
        if (sIdx < 0) sIdx = idx;
      } else
      if (sIdx >= 0) {
        // schedule[156] == 0 항상 true 이므로 별도 잔여 처리 필요없음.
        if ((idx - sIdx) >= len)
        {
          eLen += (idx - sIdx - len + 1);
          rg.add(sIdx);
          rg.add(idx - sIdx - len + 1);
        }
        sIdx = -1;
      }
    }
    if (eLen == 0) return ResponseEntity.ok().body(rs);

    int count = 9;
    if (rg.size() >= 16) count = 9; else
    if (rg.size() >= 8 && eLen <= 120) count = rg.size() / 2; else
    if (eLen <= 4) {
      count = eLen;
    } else
    if (eLen <= 60) {
      count = 4 + (int)((eLen - 20) / 10);
    }

    double delta = eLen / count;
    sIdx = -1; eLen = 0;
    for (int idx = 0, i1 = 0; idx < count; idx++) {
      int factor = (int)(idx * delta);

      while(factor - eLen > rg.get(i1 + 1)) {
        eLen += rg.get(i1 + 1);
        i1 += 2; sIdx = -1;
      }

      if (sIdx == -1) {
        sIdx = rg.get(i1);
      } else
      {
        sIdx = rg.get(i1) + factor - eLen;
        if (delta >= 6)
        {
          sIdx = (int)(sIdx / 6) * 6;
        } else
        if (delta >= 3) {
          sIdx = (int)(sIdx / 3) * 3;
        }
      }

      int hh = (int)(sIdx / 12) + 7;
      int mm = (sIdx % 12) * 5;
      rs.add(LocalTime.of(hh, mm, 0));

      System.out.println(sIdx);
    };

    return ResponseEntity.ok().body(rs);
  }

  @GetMapping("/RsvDrSchMonth/{meddr}/{medYM}/{medept}/{tx_time}/{overlap}")
  public ResponseEntity<DrMonthSch> getRsvDrSchMonth(
    @PathVariable(name = "meddr"  ) String meddr,
    @PathVariable(name = "medYM"  ) YearMonth medYM,
    @PathVariable(name = "medept" ) String medept,
    @PathVariable(name = "tx_time") Integer tx_time,
    @PathVariable(name = "overlap") char overlap
  ) {
    // RsvDrSchInfo rs = new RsvDrSchInfo();
    // List<RsvDrInfo> rdi = service.getRsvInfo(patno, meddate, medept);
    // if (!rdi.isEmpty()) {
    //   RsvDrInfo rd0 = rdi.get(0);
    //
    //   rs.setRsvInfo(rd0);
    //   rs.setMedYM(dms.getMedYM());
    //   rs.setDays(dms.getDays());
    // }

    DrMonthSch dms = service.getSchedule(meddr, medept, medYM, tx_time, overlap);
    return ResponseEntity.ok().body(dms);
  }

}
