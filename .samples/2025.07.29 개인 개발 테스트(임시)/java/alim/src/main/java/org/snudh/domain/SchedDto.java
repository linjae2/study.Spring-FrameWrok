package org.snudh.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchedDto {
  private LocalDate rsvDate;    // 예약(진료/검사)일
  private Integer   dday;       // 진료 디데이(알림발송)
  private String    msgType;    // 진료/검사 예약

  private String msgData;       // 예약 데이터
}
