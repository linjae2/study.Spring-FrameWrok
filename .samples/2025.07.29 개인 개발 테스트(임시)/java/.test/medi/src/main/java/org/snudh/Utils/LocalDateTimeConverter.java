package org.snudh.Utils;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDateTime attribute) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'convertToDatabaseColumn'");
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Date dbData) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'convertToEntityAttribute'");
  }
}
