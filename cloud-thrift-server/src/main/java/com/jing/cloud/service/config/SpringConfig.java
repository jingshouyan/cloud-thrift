package com.jing.cloud.service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * spring 配置
 * @ClassName SpringConfig
 * @Description 
 * @author jingshouyan 290173092@qq.com
 * @Date 2017年7月13日 下午6:37:41
 * @version 1.0.0
 */
@Component
public class SpringConfig implements ApplicationContextAware,EmbeddedValueResolverAware {

    private static final Logger logger = LoggerFactory.getLogger(SpringConfig.class);
    private ApplicationContext ctx;
    private StringValueResolver stringValueResolver;

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

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
    }

    public String getPropertiesValue(String key){
        key = "${"+key+"}";
        return stringValueResolver.resolveStringValue(key);
    }
}
