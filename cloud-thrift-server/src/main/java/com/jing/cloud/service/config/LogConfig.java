package com.jing.cloud.service.config;


/**
 * Created by 29017 on 2017/9/4.
 */
public class LogConfig {
    private static final String SERVICE_NAME = "service.name";
    private static final String LOG_ROOT_LEVEL = "log.root.level";
    private static final String LOG_ROOT_PATH = "log.root.path";
    public static void init() {
        System.setProperty(SERVICE_NAME,ServConf.getString(SERVICE_NAME));
        System.setProperty(LOG_ROOT_LEVEL,ServConf.getString(LOG_ROOT_LEVEL));
        System.setProperty(LOG_ROOT_PATH,ServConf.getString(LOG_ROOT_PATH));
    }
}
