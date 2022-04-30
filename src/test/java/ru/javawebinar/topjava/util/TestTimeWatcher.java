package ru.javawebinar.topjava.util;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestTimeWatcher extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(TestTimeWatcher.class);
    private static StringBuilder joinMessage = new StringBuilder();
    private static final int SECOND_IN_NANOS = 1000000000;

    @Override
    protected void finished(long nanos, Description description) {
        String message = String.format("%-30s %s ms", description.getMethodName(), testTime(nanos));
        log.info(message);
        joinMessage.append("\n").append(message);
    }

    private String testTime(long nanos) {
        return TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0
                ? TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) + " sec " +
                TimeUnit.MILLISECONDS.convert(nanos % SECOND_IN_NANOS, TimeUnit.NANOSECONDS)
                : String.valueOf(TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS));
    }

    public static void printTestTime(String clazz) {
        log.info("\n\n" + clazz + " tests complete, time spent for tests:" + joinMessage);
        joinMessage.setLength(0);
    }
}