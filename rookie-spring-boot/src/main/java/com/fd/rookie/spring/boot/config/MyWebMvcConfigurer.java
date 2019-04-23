package com.fd.rookie.spring.boot.config;

import com.fd.rookie.spring.boot.config.exception.MyExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
@Lazy
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {
    private final Logger logger = LoggerFactory.getLogger(MyWebMvcConfigurer.class);

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new MyExceptionResolver());
    }
}
