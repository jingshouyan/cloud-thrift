package com.jing.cloud.service.aop;

import com.jing.cloud.service.config.ServConf;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 29017 on 2017/8/12.
 */
@Component
@Aspect
@Slf4j(topic = "JDBC-TEMPLATE-SQL")
public class SqlLog {

    public static boolean showSql = ServConf.isSqlLogOn();

    @Pointcut("bean(*JdbcTemplate)")
    public void aspect(){}

    @Around("aspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{

        long start = System.currentTimeMillis();
        try {
            if(showSql){
                Object[] args = joinPoint.getArgs();
                log.info("sql execution starting");
                for (int i = 0; i < args.length; i++) {
                    log.info("arg.{}===>{}",i,args[i]);
                }
            }
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            if(showSql){
                long fetch = 0;
                if(result instanceof List){
                    fetch = ((List) result).size();
                }else {
                    try{
                        fetch = Long.valueOf(String.valueOf(result));
                    }catch (Exception e){}
                }
                log.info("sql execution end. use time : {}ms, fetch : {}",(end - start),fetch);
                log.info("sql execution end. result : {} ",result);
            }
            return result;
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            if(log.isErrorEnabled()){
                log.error("call {} error. use time : {}ms",joinPoint.toShortString(),(end - start),e);
            }
            throw e;
        }

    }
}
