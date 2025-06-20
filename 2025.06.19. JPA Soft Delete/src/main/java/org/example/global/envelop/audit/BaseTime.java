package org.example.global.envelop.audit;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class BaseTime {
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  protected BaseTime() {
    this.createdAt = LocalDateTime.now();
  }

  public void update() {
    this.updatedAt = LocalDateTime.now();
  }
}
