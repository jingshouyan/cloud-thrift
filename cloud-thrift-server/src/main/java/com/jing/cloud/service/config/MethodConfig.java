package com.jing.cloud.service.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.jing.cloud.service.method.Method;
import com.jing.cloud.service.methodfactory.MethodFactory;

/**
 * Method 配置
 * @ClassName MethodConfig
 * @Description 启动时自动注册 Method 到 MethodFactory
 * @author jingshouyan 290173092@qq.com
 * @Date 2017年7月13日 下午6:38:46
 * @version 1.0.0
 */
@Configuration
public class MethodConfig {

    @Autowired
    private SpringConfig springConfig;
    
    @PostConstruct
    private void init(){
        Map<String, Method> methods = springConfig.getAppCtx().getBeansOfType(Method.class);
        for(Method method:methods.values()){
            MethodFactory.register(method);
        }
    }
}
