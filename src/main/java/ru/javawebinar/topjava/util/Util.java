package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class Util {
    private Util() {
    }

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T value, @Nullable T start, @Nullable T end) {
        return (start == null || value.compareTo(start) >= 0) && (end == null || value.compareTo(end) < 0);
    }

    public static String checkEntityFormat (BindingResult result) {
        if (result.hasErrors()) {
            return result.getFieldErrors().stream()
                    .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                    .collect(Collectors.joining("<br>"));
        }
        return "";
    }

    public static String[] getCommonAppMsg() {
        return new String[]{"common.deleted",
                "common.saved",
                "common.enabled",
                "common.disabled",
                "common.errorStatus",
                "common.confirm"};
    }
}