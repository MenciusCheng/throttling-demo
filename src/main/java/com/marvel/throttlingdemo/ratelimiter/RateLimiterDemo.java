package com.marvel.throttlingdemo.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 使用RateLimiter 实现令牌桶算法
 */
@RestController
public class RateLimiterDemo {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterDemo.class);

    // 1.0 表示 每秒中生成1个令牌存放在桶中
    private RateLimiter rateLimiter = RateLimiter.create(1); // 独立线程

    // 下单请求
    @RequestMapping("/addOrder")
    public String addOrder() {
        // 1.限流判断
        // 如果在500毫秒内如果没有获取到令牌的话，则直接走服务降级处理
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            logger.info("别抢了， 在抢也是一直等待的， 还是放弃吧！！！");
            return "别抢了， 在抢也是一直等待的， 还是放弃吧！！！";
        } else {
            // 2. 业务逻辑处理
            // 3. 返回结果
            logger.info("恭喜您，抢购成功! ");
            return "恭喜您，抢购成功!";
        }
    }
}
