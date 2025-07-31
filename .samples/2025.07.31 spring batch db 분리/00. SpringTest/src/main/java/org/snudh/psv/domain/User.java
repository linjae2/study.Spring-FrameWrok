package org.snudh.psv.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User implements Persistable<String> {
// public class User {
  @Id
  private String userId;
  private String userName;
  private int age;

  @CreatedDate
  private LocalDateTime createdDate;

  @Override
  public String getId() {
    return userId;
  }

  @Override
  public boolean isNew() {
    return createdDate == null;
  }
}
