package org.snudh.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RsvInfoDto {
  private String patno;
  private String patname;
  private String meddept;
  private LocalDateTime meddate;
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

  private String refkey;

  public String getToMeddate() {
    return (dayhosp == 'Y')? rsvdate.format(DateTimeFormatter.ofPattern("M/d(E)")) :
      (rsvdate.getMinute() == 0)? rsvdate.format(DateTimeFormatter.ofPattern("M/d(E) a h시")) :
      rsvdate.format(DateTimeFormatter.ofPattern("M/d(E) a h시 m분"));
  }

  public String getToMeddr() {
    return (meddr.trim().length() > 0)? meddr + " " : "";
  }

  public String getToSendno() {
    return sendno.startsWith("15")? sendno.substring(0, 4) + "-" + sendno.substring(4):
           sendno.startsWith("02")?   "02-" + sendno.substring(2, 6) + "-" + sendno.substring(6):
           sendno;
  }

  public String getToEtc() {
    return (examtype != 'Y')? "진료예약":
    "진료예약\n ※ 진료 30분 전 영상치의학과(2층) 촬영 후에 해당과에 내원 확인바랍니다.";
  }
}
