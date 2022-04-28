package ru.javawebinar.topjava.util;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class TestTimeWatcher extends TestWatcher {
    private static final Logger log = LoggerFactory.getLogger(TestTimeWatcher.class);
    public static ArrayList<String> testTimeArray = new ArrayList<>();
    private long startTime;


    private String testTime() {
        long testTime = System.currentTimeMillis() - startTime;
        return testTime >= 1000 ? testTime / 1000 + "sec " + testTime % 1000 + "ms" : testTime % 1000 + "ms";
    }

    private void logInfo(Description description, String status, String time) {
        String message = String.format("Test name: %s(), status: %s, spent: %s", description.getMethodName(), status, time);
        log.info(message);
        testTimeArray.add(message);
    }

    @Override
    protected void starting(Description description) {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void failed(Throwable e, Description description) {
        logInfo(description, "failed", testTime());
    }

    protected void skipped(AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", "null");
    }

    @Override
    protected void succeeded(Description description) {
        logInfo(description, "success", testTime());
    }
}