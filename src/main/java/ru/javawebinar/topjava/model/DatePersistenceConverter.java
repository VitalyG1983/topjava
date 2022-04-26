package ru.javawebinar.topjava.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.util.Date;

@Converter()
public class DatePersistenceConverter implements
        AttributeConverter<Date, Timestamp> {
    @Override
    public java.sql.Timestamp convertToDatabaseColumn(Date entityValue) {
        return new Timestamp(entityValue.getTime());
    }

    @Override
    public Date convertToEntityAttribute(java.sql.Timestamp databaseValue) {
        return new Date(databaseValue.getTime());
    }
}