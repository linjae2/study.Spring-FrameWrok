package org.snudh.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
  private char examyn;      // 당일검사여부
  private char sendyn;      // 전송여부

  public String getToMeddate() {
    return (dayhosp == 'Y')? rsvdate.format(DateTimeFormatter.ofPattern("M/d(E)")) :
      (rsvdate.getMinute() == 0)? rsvdate.format(DateTimeFormatter.ofPattern("M/d(E) a h시")) :
      rsvdate.format(DateTimeFormatter.ofPattern("M/d(E) a h시 m분"));
  }

  public String getToMeddr() {
    return (meddr.trim().length() > 0)? meddr + " " : "";
  }

  public String getEtc() {
    return (examyn != 'Y')? "진료예약":
    "진료예약\n ※ 진료 30분 전 영상치의학과(2층) 촬영 후에 해당과에 내원 확인바랍니다.";
  }
}
