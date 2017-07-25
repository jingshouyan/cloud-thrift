package com.jing.cloud.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jing.cloud.service.config.ServConf;

@SpringBootApplication
public class App{
    private static final String SERVICE_NAME = "service.name";
    private static final String LOG_ROOT_LEVEL = "log.root.level";
    private static final String LOG_ROOT_PATH = "log.root.path";
    public static void main(String[] args) {
        System.setProperty(SERVICE_NAME,ServConf.getString(SERVICE_NAME));
        System.setProperty(LOG_ROOT_LEVEL,ServConf.getString(LOG_ROOT_LEVEL));
        System.setProperty(LOG_ROOT_PATH,ServConf.getString(LOG_ROOT_PATH));
        SpringApplication.run(App.class, args);
    }
}