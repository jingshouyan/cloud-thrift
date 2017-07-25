package com.jing.cloud.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring 配置
 * @ClassName SpringConfig
 * @Description 
 * @author jingshouyan 290173092@qq.com
 * @Date 2017年7月13日 下午6:37:41
 * @version 1.0.0
 */
@Component
public class SpringConfig implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringConfig.class);
    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(ctx==null){
            ctx = applicationContext;
            logger.info("ApplicationContext load.");
        }
    }
    
    public ApplicationContext getAppCtx(){
        return ctx;
    }

}
