package com.jing.cloud.service;

import com.jing.cloud.service.config.LogConfig;
import com.jing.cloud.service.config.ServConf;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        LogConfig.init();
        SpringApplication.run(App.class, args);
    }
}