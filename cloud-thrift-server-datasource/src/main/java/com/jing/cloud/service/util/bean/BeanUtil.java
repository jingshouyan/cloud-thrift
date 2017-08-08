package com.jing.cloud.service.util.bean;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 29017 on 2017/7/29.
 */
public class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);


    public static Map<String,Object> Obj2Map(Object obj){
        if(null==obj)return null;
        Map<String,Object> map = new HashMap<>();
        for(Class c = obj.getClass();Object.class!=c;c=c.getSuperclass()){
            Field[] fields =c.getDeclaredFields();
            for(Field field:fields){
                int mod = field.getModifiers();
                if(Modifier.isStatic(mod)){
                    continue;
                }
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try{
                    map.put(field.getName(),field.get(obj));
                }catch(IllegalAccessException e){
                    logger.error("Obj2Map error. field:[{}]",field.getName(),e);
                }
                field.setAccessible(accessible);
            }
        }
        return map;
    }
}
