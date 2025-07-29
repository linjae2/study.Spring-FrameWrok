package org.snudh.ocs;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="appatbat")
public class Patient {
  @Id
  private String patno;
  private String patname;
  private LocalDateTime editdate;
}
