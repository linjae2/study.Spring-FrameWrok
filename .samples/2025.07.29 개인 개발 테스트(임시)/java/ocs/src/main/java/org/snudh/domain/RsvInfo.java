package org.snudh.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RsvInfo {
  private String patno;
  private String patname;
  private String meddept;
  private LocalDateTime rsvdate;     // 실제 예약 일시
  private String phoneno;
  private String sendno;
  private String meddrno;             // 진료의 사원번호
  private String meddrnm;             // 진료의 성명 + 직급
  private String depttogo;            // 가야할 곳
  private String deptname;            // 진료과명
  private String ordfloor;
  private String remarks;
  private String cemarks;
  private char dayhosp;     // 낮병동(당일입원)
  private char examtype;    // 당일검사여부, 영상 검사 예약 종류
  private char sendyn;      // 전송여부

  public RsvInfo(
    String patno,
    String patname,
    String meddept,
    Date rsvdate,
    String phoneno,
    String sendno,
    String meddrno,
    String meddrnm,
    String depttogo,
    String deptname,
    String ordfloor,
    String remarks,
    String cemarks,
    char dayhosp,
    char examtype,
    char sendyn
  ) {
    this.patno = patno;
    this.patname = patname;
    this.meddept = meddept;
    this.rsvdate = Instant.ofEpochMilli(rsvdate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    this.phoneno = phoneno;
    this.sendno = sendno;
    this.meddrno = meddrno;
    this.meddrnm = meddrnm;
    this.depttogo = depttogo;
    this.deptname = deptname;
    this.ordfloor = ordfloor;
    this.remarks = remarks;
    this.cemarks = cemarks;
    this.dayhosp = dayhosp;
    this.examtype = examtype;
    this.sendyn = sendyn;
  }
}
