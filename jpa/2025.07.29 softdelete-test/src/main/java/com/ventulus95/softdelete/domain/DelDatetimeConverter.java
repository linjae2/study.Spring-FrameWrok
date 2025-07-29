package com.ventulus95.softdelete.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;

@Converter
public class DelDatetimeConverter implements AttributeConverter<Boolean, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(final Boolean aBoolean) {
        return LocalDateTime.now();
    }

    @Override
    public Boolean convertToEntityAttribute(final LocalDateTime localDateTime) {
        return localDateTime != null;
    }
}
