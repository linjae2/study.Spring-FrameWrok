package org.snudh.ocs.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RsvDrInfo {
  public String rsvdr;
  public String drname;
  public String meddept;
  public Integer tx_time;
  public char overlap;
}
