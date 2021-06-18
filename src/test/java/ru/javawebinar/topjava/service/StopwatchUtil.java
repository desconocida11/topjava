package ru.javawebinar.topjava.service;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StopwatchUtil extends Stopwatch {

    private static final Logger LOGGER = Logger.getLogger(StopwatchUtil.class.getName());

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        String message = String.format("Test %s %s, spent %d milliseconds%n",
                testName, status, TimeUnit.NANOSECONDS.toMillis(nanos));
//        if (LOGGER.isLoggable(Level.FINEST))
        LOGGER.log(Level.FINE, message);
    }

    @Override
    public long runtime(TimeUnit unit) {
        return super.runtime(unit);
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "finished", nanos);
    }
}
