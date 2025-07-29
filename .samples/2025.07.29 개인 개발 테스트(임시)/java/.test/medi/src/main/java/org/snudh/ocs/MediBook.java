package org.snudh.ocs;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MediBook {
  private String patno;
  private String patname;
  private String meddept;
  private LocalDateTime rsvdate;
  private String phoneno;
  private String sendno;
  private String meddr;
  private String deptname;
  private String ordfloor;
  private String remarks;
  private String cemarks;
  private char dayhosp;     // 낮병동(당일입원)
  private char examtype;    // 당일검사여부, 영상 검사 예약 종류
  private char sendyn;      // 전송여부

  public MediBook(
    String patno,
    String patname,
    String meddept,
    Date rsvdate,
    String phoneno,
    String sendno,
    String meddr,
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
    this.meddr = meddr;
    this.deptname = deptname;
    this.ordfloor = ordfloor;
    this.remarks = remarks;
    this.cemarks = cemarks;
    this.dayhosp = dayhosp;
    this.examtype = examtype;
    this.sendyn = sendyn;
  }
}
