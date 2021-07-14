package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public final class LocalTimeFormatter implements Formatter<LocalTime> {

    private final DateTimeFormatter formatter = ISO_LOCAL_TIME;

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return LocalTime.parse(text, formatter);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.format(formatter);
    }
}
