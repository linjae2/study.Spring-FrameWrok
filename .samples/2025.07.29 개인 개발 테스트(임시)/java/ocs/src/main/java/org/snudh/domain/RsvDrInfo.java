package org.snudh.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RsvDrInfo {
  public String rsvdr;
  public String drname;
  public String meddept;
  public Integer tx_time;
  public char overlap;
  public char change_yn;
}
