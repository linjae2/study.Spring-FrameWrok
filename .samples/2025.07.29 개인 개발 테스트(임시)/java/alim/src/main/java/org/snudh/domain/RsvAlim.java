package org.snudh.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rsv_alim")
@Builder
public class RsvAlim {
  @Getter
  @Setter
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Key implements Serializable {
    private LocalDate rsvDate;         // 예약(진료/검사)일
    private Integer   dDay;           // 진료 디데이(알림발송)
    private String    msgType;        // 진료/검사 예약
    private Integer   seq;            // 등록 순번
  }

  @Embeddable
  public record PKey(
    LocalDate rsvDate,
    Integer   dDay,
    String    msgType,
    Integer   seq
  ) {}

  @EmbeddedId
  private Key key;
  private String patno;
  private String patname;
  private String meddept;           // 진료과 코드
  private LocalDateTime meddate;    // 실 예약 일시
  private String phoneno;
  private String sendno;
  private String meddrno;
  private String alimdrnm;
  private String deptname;
  private String depttogo;
  private String ordfloor;
  private String remarks;
  private String cemarks;
  private char dayhosp;     // 낮병동(당일입원)
  private char examtype;    // 당일검사여부, 영상 검사 예약 종류
  private char sendyn;      // 전송여부

  @Column(nullable = false)
  @CreationTimestamp
  private LocalDateTime cdate;    // 생성일시

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime mdate;    // 수정일시

  public static RsvAlim of(LocalDate rsvDate, Integer dDay, String msgType, Integer seq, RsvInfoDto dto) {
    return builder().key(
      new Key(rsvDate, dDay, msgType, seq))
    .patno(dto.getPatno()).patname(dto.getPatname())
    .meddept(dto.getMeddept())
    .meddate(dto.getRsvdate())
    .phoneno(dto.getPhoneno())
    .sendno(dto.getSendno())
    .meddrno(dto.getMeddrno())
    .alimdrnm(dto.getMeddrnm())
    .deptname(dto.getDeptname())
    .depttogo(dto.getDepttogo())
    .ordfloor(dto.getOrdfloor())
    .remarks(dto.getRemarks())
    .cemarks(dto.getCemarks())
    .dayhosp(dto.getDayhosp())
    .examtype(dto.getExamtype())
    .sendyn(dto.getSendyn())
    .build();
  }
}
