package com.jing.cloud.service.util.db;

import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by 29017 on 2017/8/4.
 */
@Slf4j
public class DbInfoUtil {

    private static final ConcurrentMap<Class,Map<Class,Field>> cache = new ConcurrentHashMap<>();


    /**
     * 获取对象的 @Key 属性名
     * @param clazz 对象类型
     * @return  @Key 属性名
     */
    public static String getKeyName(Class<?> clazz){
        Field field = getField(clazz,Key.class);
        if(null!=field){
            return field.getName();
        }
        return null;
    }

    /**
     * 获取对象的 @Version 属性名
     * @param clazz 对象类型
     * @return  @Key 属性名
     */
    public static String getVersionName(Class<?> clazz){
        Field field = getField(clazz,Version.class);
        if(null!=field){
            return field.getName();
        }
        return null;
    }

    /**
     * 设置 @Key 值
     * @param obj 对象
     * @param key 值
     */
    @SneakyThrows
    public static void setKey(Object obj,long key){
        Field field = getField(obj.getClass(),Key.class);
        if(null!=field){
            boolean accessible = field.isAccessible();
            if(!accessible){
                field.setAccessible(true);
            }
            Class<?> clazz = field.getType();
            if(Long.class == clazz){
                field.set(obj,key);
            } else if (String.class == clazz){
                field.set(obj,String.valueOf(key));
            }
            if(!accessible){
                field.setAccessible(false);
            }
        }
    }

    /**
     * 获取 @Key 字段的值
     * 如果没有设置 @Key 返回 null
     * @param obj 对象
     * @return @Key 字段的值
     */
    @SneakyThrows
    public static Object getKey(Object obj){
        Object result = null;
        Field field = getField(obj.getClass(),Key.class);
        if(null!=field){
            boolean accessible = field.isAccessible();
            if(!accessible){
                field.setAccessible(true);
            }
            result = field.get(obj);
            if(!accessible){
                field.setAccessible(false);
            }
        }
        return result;
    }


    @Synchronized
    private static Field getField(Class<?> ObjClazz,Class<? extends Annotation> annotationClazz){
        Field f = null;
        Map<Class,Field> anMap = getMap(annotationClazz);
        if(anMap.containsKey(ObjClazz)){
            f = anMap.get(ObjClazz);
        }else{
            loop:for(Class<?> c=ObjClazz;c!=Object.class;c=c.getSuperclass()){
                Field[] fields = c.getDeclaredFields();
                for(Field field :fields){
                    if(field.isAnnotationPresent(annotationClazz)){
                        f = field;
                        break loop;
                    }
                }
            }
            log.info("[{}] {} field is [{}]",ObjClazz,annotationClazz.getSimpleName(),f);
            anMap.put(ObjClazz,f);
        }
        return f;
    }

    private static Map<Class,Field> getMap(Class clazz){
        Map<Class,Field> anMap = cache.get(clazz);
        if(null==anMap){
            anMap = new HashMap<>();
            Map<Class,Field> m = cache.putIfAbsent(clazz,anMap);
            if(null!=m){
                anMap = m;
            }
        }
        return anMap;
    }
}
