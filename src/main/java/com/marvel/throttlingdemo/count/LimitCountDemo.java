package com.marvel.throttlingdemo.count;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器限流
 */
public class LimitCountDemo {
    private int limtCount = 3;// 限制最大访问的容量
    AtomicInteger atomicInteger = new AtomicInteger(0); // 每秒钟 实际请求的数量
    private boolean isReset = false;
    private long start = System.currentTimeMillis();// 获取当前系统时间
    private int interval = 3000;// 间隔时间
    private final ExecutorService newCachedThreadPool = Executors.newCachedThreadPool(); // 线程池

    private static final Logger logger = LoggerFactory.getLogger(LimitCountDemo.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    private synchronized boolean acquire() {
        long newTime = System.currentTimeMillis();
        if (newTime > (start + interval) && !isReset) {
            isReset = true;
            // 判断是否是一个周期
            start = newTime;
            logger.info("start=" + sdf.format(new Date(start)));
            atomicInteger.set(0); // 清理为0
        }
        if (isReset) {
            isReset = false;
        }
        int i = atomicInteger.incrementAndGet();// i++;
        logger.info("atomicInteger=" + i);
        return i <= limtCount;
    }

    private void access() {
        for (int i = 1; i <= 6; i++) {
            final int tempI = i;
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    if (acquire()) {
                        logger.info("你没有被限流,可以正常访问逻辑 i:" + tempI);
                    } else {
                        logger.info("你已经被限流呢  i:" + tempI);
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LimitCountDemo limitService = new LimitCountDemo();
        logger.info("start=" + sdf.format(new Date(limitService.start)));
        limitService.access();
        Thread.sleep(3100);
        System.out.println("==================");
        limitService.access();
        Thread.sleep(3100);
        System.out.println("==================");
        limitService.access();
        limitService.newCachedThreadPool.shutdown();
    }
}
