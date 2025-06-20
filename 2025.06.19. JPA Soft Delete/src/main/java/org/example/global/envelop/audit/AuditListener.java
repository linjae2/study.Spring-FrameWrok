package org.example.global.envelop.audit;

import java.util.Optional;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener {
  @PrePersist
  public void setCreatedAt(final Auditable auditable) {
    final BaseTime baseTime =
      Optional.ofNullable(auditable.getBaseTime()).orElseGet(BaseTime::new);
    auditable.setBaseTime(baseTime);
  }

  @PreUpdate
  public void setUpdatedAt(final Auditable auditable) {
    auditable.getBaseTime().update();
  }
}
