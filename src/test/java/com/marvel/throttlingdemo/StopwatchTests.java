package com.marvel.throttlingdemo;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Marvel on 18/10/25.
 */
public class StopwatchTests {

    private static final Logger logger = LoggerFactory.getLogger(StopwatchTests.class);

    @Test
    public void test1() throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(2000);
        stopwatch.stop();
        long duration = stopwatch.elapsed(MILLISECONDS);
        logger.info("duration=" + duration);
    }
}
