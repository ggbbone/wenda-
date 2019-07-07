package com.yzg.toutiao.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author yzg
 * @create 2019/6/26
 *
 * Spring AOP 实现日志
 */
@Component
public class LogAspect {

    private static final Logger LOGGER=LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.yzg.toutiao.controller.IndexController.*(..))")
    public void beforeMethod(){
        LOGGER.info("before-------------:");
    }

    @After("execution(* com.yzg.toutiao.controller.IndexController.*(..))")
    public void afterMethod(){
        LOGGER.info("after--------------:");
    }
}
