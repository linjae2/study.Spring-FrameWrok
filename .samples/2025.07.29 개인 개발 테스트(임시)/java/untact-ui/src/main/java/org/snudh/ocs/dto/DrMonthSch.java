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
public class DrMonthSch {
  YearMonth medYM;
  LocalDate nextmed;
  int[] days;
}
