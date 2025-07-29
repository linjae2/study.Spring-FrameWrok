package org.snudh.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DrRsvInfo {
  public LocalDateTime rsvdate;
  public int tx_time;
  public char overlap;

  public DrRsvInfo(
    Date rsvdate,
    int tx_time,
    char overlap
  ) {
    this.rsvdate = Instant.ofEpochMilli(rsvdate.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    //this.rsvdr = rsvdr;
    //this.meddept = meddept;
    this.tx_time = tx_time;
    this.overlap = overlap;
  }
}
