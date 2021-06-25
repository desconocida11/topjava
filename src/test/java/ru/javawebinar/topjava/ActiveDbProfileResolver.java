package ru.javawebinar.topjava;

import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfilesResolver;
import org.springframework.util.ClassUtils;

import static ru.javawebinar.topjava.util.Profiles.HSQL_DB;
import static ru.javawebinar.topjava.util.Profiles.POSTGRES_DB;

public class ActiveDbProfileResolver implements ActiveProfilesResolver {

    //  Get DB profile depending of DB driver in classpath
    public static String getDb() {
        if (ClassUtils.isPresent("org.postgresql.Driver", null)) {
            return POSTGRES_DB;
        } else if (ClassUtils.isPresent("org.hsqldb.jdbcDriver", null)) {
            return HSQL_DB;
        } else {
            throw new IllegalStateException("Could not find DB driver");
        }
    }

    @Override
    public @NonNull
    String[] resolve(@NonNull Class<?> aClass) {
        return new String[]{getDb()};
    }
}
