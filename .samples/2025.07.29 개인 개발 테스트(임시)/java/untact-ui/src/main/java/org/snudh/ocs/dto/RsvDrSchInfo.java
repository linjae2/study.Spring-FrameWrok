package org.snudh.ocs.dto;

import java.time.LocalDate;
import java.time.YearMonth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsvDrSchInfo {
  RsvDrInfo rsvInfo;
  YearMonth medYM;
  LocalDate nextMED;
  int[] days;
}
