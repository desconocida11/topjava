package ru.javawebinar.topjava.web.formatters;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class LocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter = ISO_LOCAL_DATE;

    @Override
    public LocalDate convert(String s) {
        return LocalDate.parse(s, formatter);
    }
}
