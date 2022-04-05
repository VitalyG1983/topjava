package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenTime(LocalTime value, LocalTime start, LocalTime end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) < 0;
    }

    public static boolean isBetweenDate(LocalDate value, LocalDate start, LocalDate end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

  /*  }  public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, T start, T end) {
        return value.compareTo(start) >= 0
                && (end.getClass() == LocalDate.class ? value.compareTo(end) <= 0 : value.compareTo(end) < 0);
    }*/

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}