package org.snudh.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public class DrSchedule {
  public LocalDate medddate;
  public String am_schedule;
  public String pm_schedule;
  public char am_bookyn;
  public char pm_bookyn;
  public int am_usechair;
  public int pm_usechair;

  public DrSchedule(
    Date medddate,
    String am_schedule,
    String pm_schedule,
    char am_bookyn,
    char pm_bookyn,
    int am_usechair,
    int pm_usechair
  )
  {
    this.medddate = Instant.ofEpochMilli(medddate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    this.am_schedule = am_schedule;
    this.pm_schedule = pm_schedule;
    this.am_bookyn = am_bookyn;
    this.pm_bookyn = pm_bookyn;
    this.am_usechair = am_usechair;
    this.pm_usechair = pm_usechair;
  }
}

