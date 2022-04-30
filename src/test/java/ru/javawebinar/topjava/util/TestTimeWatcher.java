package ru.javawebinar.topjava.util;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestTimeWatcher extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(TestTimeWatcher.class);
    private static String JoinedMessage;

    protected void finished(long nanos, Description description) {
        logInfo(description, testTime(nanos));
    }

    private String testTime(long nanos) {
        return TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0 ? TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) + " sec " +
                TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) + " ms" : TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) + " ms";
    }

    private void logInfo(Description description, String time) {
        String message = String.format("%-30s %-20s", description.getMethodName(), time);
        log.info(message);
        JoinedMessage = String.join("\n", JoinedMessage == null ? "" : JoinedMessage, message);
    }

    public static void setJoinedMessage(String joinedMessage) {
        JoinedMessage = joinedMessage;
    }

    public static String getJoinedMessage() {
        return JoinedMessage;
    }
}