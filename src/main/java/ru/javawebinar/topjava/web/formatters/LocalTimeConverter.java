package ru.javawebinar.topjava.web.formatters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

public class LocalTimeConverter implements Converter<String, LocalTime> {

    private final DateTimeFormatter formatter = ISO_LOCAL_TIME;

    @Override
    public LocalTime convert(String s) {
        return LocalTime.parse(s, formatter);
    }
}
