package com.jing.cloud.service.util.bean;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by 29017 on 2017/7/29.
 */
@Slf4j
public class BeanUtil {


    private static final ConcurrentMap<Class<?>,Set<Field>> cache = Maps.newConcurrentMap();


    @SneakyThrows
    public static Map<String,Object> Obj2Map(Object obj){
        if(null==obj)return null;
        Map<String,Object> map = Maps.newHashMap();
        Set<Field> fieldSet = fieldSet(obj.getClass());
        for (Field field:fieldSet) {
            boolean accessable = field.isAccessible();
            if(!accessable){
                field.setAccessible(true);
            }
            map.put(field.getName(),field.get(obj));
            if(!accessable){
                field.setAccessible(false);
            }
        }
        return map;
    }


    public static Set<String> fieldNameSet(Class<?> clazz){
        Set<String> fieldNames = Sets.newHashSet();
        Set<Field> fieldSet = fieldSet(clazz);

        Collection<String> c = Collections2.transform(fieldSet, new Function<Field, String>() {
            @Override
            public String apply(Field field) {
                return field.getName();
            }
        });
        fieldNames.addAll(c);
        return fieldNames;
    }

    /**
     *
     * @param clazz 类型
     * @return 该类型所有的属性值
     */
    private static Set<Field> fieldSet(Class<?> clazz){
        //根据类型加锁
        synchronized (clazz){
            Set<Field> fieldSet = cache.get(clazz);
            if(null==fieldSet){
                fieldSet = Sets.newConcurrentHashSet();
                Set<Field> set = cache.putIfAbsent(clazz,fieldSet);
                if(null!=set){
                    fieldSet = set;
                }
                //属性名 用于排除 重名的 父类中的属性
                Set<String> fieldNames = Sets.newConcurrentHashSet();

                for(Class c = clazz;Object.class!=c;c=c.getSuperclass()){
                    Field[] fields =c.getDeclaredFields();
                    for(Field field:fields){
                        int mod = field.getModifiers();
                        if(Modifier.isStatic(mod)){
                            continue;
                        }
                        if(!fieldNames.contains(field.getName())){
                            fieldNames.add(field.getName());
                            fieldSet.add(field);
                        }
                    }
                }

            }
            return fieldSet;
        }
    }
}
