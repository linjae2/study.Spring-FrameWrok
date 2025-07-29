package org.snudh.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pat_token")
@Builder
public class PatToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String patno;
  private LocalDateTime rsvdate;
  private String meddept;

  private String patName;
  private String deptnm;
  private String alimdrnm;

  private int tokVer;
  private LocalDate expired;
  private String token;

  @Column(nullable = false)
  @CreationTimestamp
  private LocalDateTime cdate;    // 생성일시
}
