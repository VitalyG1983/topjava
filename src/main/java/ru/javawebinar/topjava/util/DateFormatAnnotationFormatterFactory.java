package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

final class LocalTimeFormatter implements Formatter<LocalTime> {

    @Override
    public String print(LocalTime time, Locale locale) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public LocalTime parse(String formatted, Locale locale) {
        if (!StringUtils.hasText(formatted)) return null;
        return LocalTime.parse(formatted);
    }
}

final class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public String print(LocalDate date, Locale locale) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public LocalDate parse(String formatted, Locale locale) {
        if (!StringUtils.hasText(formatted)) return null;
        return LocalDate.parse(formatted);
    }
}