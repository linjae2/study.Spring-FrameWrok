package org.snudh.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "alim_tmpl")
@Builder
public class AlimTmpl {
  @Id
  private String id;

  @Column(length=20, nullable=false)
  private String cd;
  
  @Column(length = 1000, nullable = false)
  private String tx;

  @Column(length = 1000, nullable = false)
  private String at;

  @Column(nullable = false)
  @UpdateTimestamp
  private LocalDateTime mdate;    // 수정일시
}
