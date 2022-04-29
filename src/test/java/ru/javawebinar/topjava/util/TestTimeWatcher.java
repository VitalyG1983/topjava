package ru.javawebinar.topjava.util;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class TestTimeWatcher extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(TestTimeWatcher.class);
    public static ArrayList<String> testTimeArray = new ArrayList<>();

    private String testTime(long nanos) {
        return nanos >= 1000000000 ? nanos / 1000000000 + " sec " +
                (nanos % 1000000000) / 1000000 + " ms" : (nanos % 1000000000) / 1000000 + " ms";
    }

    private void logInfo(Description description, String time) {
        String message = String.format("%-30s %-20s", description.getMethodName(), time);
        log.info(message);
        testTimeArray.add(message);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, testTime(nanos));
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "null");
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, testTime(nanos));
    }
}