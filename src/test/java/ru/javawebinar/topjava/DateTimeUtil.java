package ru.javawebinar.topjava;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static LocalDate START_DATE = LocalDate.of(2020, 1, 30);
    public static LocalDate END_DATE = LocalDate.of(2020, 2, 1);
    public static LocalTime START_TIME = LocalTime.of(13, 0);
    public static LocalTime END_TIME = LocalTime.of(20, 1);

    private DateTimeUtil() {
    }

    public static String getFormattedDate(LocalDate localDate) {
        return localDate == null ? "" : DateTimeFormatter.ISO_DATE.format(localDate);
    }

    public static String getFormattedTime(LocalTime localTime) {
        return localTime == null ? "" : DateTimeFormatter.ISO_TIME.format(localTime);
    }
}
