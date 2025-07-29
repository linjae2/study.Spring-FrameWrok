package org.snudh.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
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
import lombok.experimental.SuperBuilder;

// @NamedStoredProcedureQuery(
//   name = "proc_event_rsv_alim_seq",
//   procedureName = "event_rsv_alim_seq",
//   parameters = {
//     @StoredProcedureParameter(name = "rsv_date", mode = ParameterMode.IN, type = LocalDate.class),
//     @StoredProcedureParameter(name = "d_date", mode = ParameterMode.OUT, type = Integer.class),
//     @StoredProcedureParameter(name = "d_date", mode = ParameterMode.OUT, type = String.class),
//     @StoredProcedureParameter(name = "d_date", mode = ParameterMode.OUT, type = Integer.class),
//   }
// )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_rsv_alim")
@Builder
public class SchedAlim {
  @Getter
  @Setter
  @Embeddable
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Key implements Serializable {
    private LocalDate rsvDate;    // 예약(진료/검사)일
    private Integer   dDay;       // 진료 디데이(알림발송)
    private String    msgType;    // 진료/검사 예약
    private Integer   seq;        // 키 순번
  }

  @EmbeddedId
  private Key key;

  @Column(nullable = false)
  @CreationTimestamp
  private LocalDateTime cdate;    // 생성일시

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime mdate;    // 수정일시

  @Column(nullable = false)
  private Integer msg_st;

  //@Type(type="tecxt")
  @Column(columnDefinition="TEXT", nullable = false)
  private String msgData;

  public static SchedAlim of(SchedDto dto) {
    return builder().key(
      new Key(dto.getRsvDate(), dto.getDday(), dto.getMsgType(), null))
    .msgData(dto.getMsgData())
    .msg_st(1)
    .build();
  }
}
