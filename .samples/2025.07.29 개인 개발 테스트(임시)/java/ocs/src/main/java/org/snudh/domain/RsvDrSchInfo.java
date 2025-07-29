package org.snudh.domain;

import java.time.LocalDate;
import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RsvDrSchInfo {
  RsvDrInfo rsvInfo;
  YearMonth medYM;
  LocalDate nextMED;
  int[] days;
}
