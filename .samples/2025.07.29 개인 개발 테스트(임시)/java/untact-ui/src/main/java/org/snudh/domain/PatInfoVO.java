package org.snudh.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatInfoVO {
  private String patno;
  private String patname;
  private LocalDateTime meddate;
  private String meddept;
  private String deptnm;
  private String meddrnm;
  private LocalDate expired;
}
