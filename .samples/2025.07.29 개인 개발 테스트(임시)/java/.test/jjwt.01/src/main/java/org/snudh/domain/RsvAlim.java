package org.snudh.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
  public static class PKey implements Serializable {
    private LocalDate rsvDay;     // 예약(진료/검사)일
    private Integer   dDay;       // 진료 디데이(알림발송)
    private String    msgType;    // 진료/검사 예약
    private Integer   sequence;        // 등록 순번
  }

  @Embeddable
  public record Key(
    LocalDate rsvDay,
    Integer   dDay,
    String    msgType,
    Integer   sequence
  ) {}

  @EmbeddedId
  private Key key;
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

  public static RsvAlim of(LocalDate rsvDay, Integer dDay, String msgType, Integer sequence, RsvInfoDto dto) {
    return builder().key(
      new Key(rsvDay, dDay, msgType, sequence))
    .patno(dto.getPatno()).patname(dto.getPatname())
    .build();
  }
}
