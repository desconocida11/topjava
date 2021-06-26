package ru.javawebinar.topjava;

import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfilesResolver;

import static ru.javawebinar.topjava.util.Profiles.getDb;

public class ActiveDbProfileResolver implements ActiveProfilesResolver {

    @Override
    public @NonNull
    String[] resolve(@NonNull Class<?> aClass) {
        return new String[]{getDb()};
    }
}
