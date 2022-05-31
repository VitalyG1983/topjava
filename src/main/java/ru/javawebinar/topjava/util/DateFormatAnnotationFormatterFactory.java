package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static java.util.Arrays.asList;

public final class DateFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<DateTimeFormat> {

    public DateFormatAnnotationFormatterFactory() {
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(asList(new Class<?>[]{
                LocalDate.class, LocalTime.class,}));
    }

    @Override
    public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter configureFormatterFrom(DateTimeFormat annotation, Class<?> fieldType) {
        if (fieldType.equals(LocalDate.class)) {
            return new LocalDateFormatter();
        } else if (fieldType.equals(LocalTime.class)) {
            return new LocalTimeFormatter();
        } else
            throw new IllegalArgumentException("Cant parse from " + fieldType + " fieldType via @DateTimeFormat");
    }
}

final class LocalTimeFormatter implements Formatter<LocalTime> {

    public LocalTimeFormatter() {
    }

    @Override
    public String print(LocalTime time, Locale locale) {
        return time.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public LocalTime parse(String formatted, Locale locale) throws ParseException {
        if (formatted.length() == 0) return null;
        return getDateFormat().parse(formatted, locale).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    private DateFormatter getDateFormat() {
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("HH:mm");
        return dateFormatter;
    }
}

final class LocalDateFormatter implements Formatter<LocalDate> {

    public LocalDateFormatter() {
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public LocalDate parse(String formatted, Locale locale) throws ParseException {
            if (formatted.length() == 0) return null;
        return getDateFormat().parse(formatted, locale).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private DateFormatter getDateFormat() {
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setIso(DateTimeFormat.ISO.DATE);
        return dateFormatter;
    }
}