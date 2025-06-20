package org.example.global;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.persistence.AttributeConverter;

public class DeletedAtConverter implements AttributeConverter<Boolean, LocalDateTime> {

  @Override
  public LocalDateTime convertToDatabaseColumn(Boolean attribute) {
    return LocalDateTime.now();
  }

  @Override
  public Boolean convertToEntityAttribute(LocalDateTime dbData) {
    return Optional.ofNullable(dbData).isPresent();
  }
}
