package com.yzg.toutiao.configuration;

import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.interceptor.LoginRequredInterceptor;
import com.yzg.toutiao.interceptor.PassportInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author yzg
 * @create 2019/7/6
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginRequredInterceptor loginRequredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/users/**");
        super.addInterceptors(registry);
    }
}
