package com.jing.cloud.service.util.db;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jing.cloud.service.util.bean.StrFormat;
import com.jing.cloud.service.util.db.annotation.Column;
import com.jing.cloud.service.util.db.annotation.Key;
import com.jing.cloud.service.util.db.annotation.Table;
import com.jing.cloud.service.util.db.annotation.Version;
import com.jing.cloud.service.util.keygen.DefaultKeyGenerator;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * java bean 数据库表工具类
 * Created by 29017 on 2017/8/10.
 */
@Slf4j
public class Bean4DbUtil {
    private static final ConcurrentMap<Class<?>,BeanTable> beanMap = Maps.newConcurrentMap();

    public static Set<String> getFieldNameSet(Class<?> clazz){
        return getBeanTable(clazz).getFieldNameMap().keySet();
    }

    public static Map<String,String> fieldColumnMap(Class<?> clazz){

        return Maps.transformValues(getBeanTable(clazz).getFieldNameMap(), new Function<BeanColumn, String>() {
            @Override
            public String apply(BeanColumn input) {
                return input.getColumnName();
            }
        });
    }

    public static String getTableName(Class<?> clazz){
        return getBeanTable(clazz).getTableName();
    }

    /**
     * 获取 class 定义的 主键 名
     * @param clazz class
     * @return 主键 名
     */
    public static String getKeyFieldName(Class<?> clazz){
        BeanColumn key = getBeanTable(clazz).getKey();
        Preconditions.checkArgument(null!=key,"class["+clazz+"] is not set key");
        return key.getFieldName();
    }

    public static String getVersionFieldName(Class<?> clazz){
        BeanColumn version = getBeanTable(clazz).getKey();
        if(null==version){
            return null;
        }
        return version.getFieldName();
    }


    @SneakyThrows
    public static void genKey(Object bean){
        BeanColumn key =  getBeanTable(bean.getClass()).getKey();
        if(null!=key&&key.isAutoGen()){
            Field field = key.getField();
            boolean accessible = field.isAccessible();
            if(!accessible){
                field.setAccessible(true);
            }
            Object fieldValue = field.get(bean);
            if(null==fieldValue){
                Class<?> clazz =field.getType();
                if(clazz==Long.class||clazz==long.class){
                    field.set(bean, DefaultKeyGenerator.getInstance().generateKey().longValue());
                }else if(clazz==Integer.class||clazz==int.class){
                    field.set(bean, DefaultKeyGenerator.getInstance().generateKey().intValue());
                }else if(clazz==String.class){
                    field.set(bean,String.valueOf(DefaultKeyGenerator.getInstance().generateKey().longValue()));
                }else{
                    if(!accessible){
                        field.setAccessible(false);
                    }
                    throw new IllegalArgumentException("key["+field.getName()+"] type must be int|long|string.");
                }
            }
            if(!accessible){
                field.setAccessible(false);
            }
        }
    }

    @SneakyThrows
    public static Map<String,Object> Bean2Map(Object bean){
        if(null == bean) return null;
        Map<String,Object> map = Maps.newHashMap();
        BeanTable beanTable = getBeanTable(bean.getClass());
        for (Field field: beanTable.getFieldMap().keySet()) {
            boolean accessible = field.isAccessible();
            if(!accessible){
                field.setAccessible(true);
            }
            map.put(field.getName(),field.get(bean));
            if(!accessible){
                field.setAccessible(false);
            }
        }
        return map;
    }

    public static String getColumnName(Class<?> clazz,String fieldName){
        BeanTable beanTable = getBeanTable(clazz);
        BeanColumn beanColumn = beanTable.getFieldNameMap().get(fieldName);
        Preconditions.checkArgument(null!=beanColumn
                ,String.format("[%s] dos not contains field [%s]",clazz,fieldName));
        return beanColumn.getColumnName();
    }

    @Synchronized
    private static BeanTable getBeanTable(Class<?> clazz){
        BeanTable beanTable = beanMap.get(clazz);
        if(null==beanTable){
            beanTable = clazz2BeanTable(clazz);
            BeanTable bt = beanMap.putIfAbsent(clazz,beanTable);
            if(null!=bt){
                beanTable = bt;
            }
        }
        return beanTable;
    }

    private static BeanTable clazz2BeanTable(Class<?> clazz){
        BeanTable beanTable = new BeanTable();
        beanTable.setClazz(clazz);
        Table table = clazz.getAnnotation(Table.class);
        if(null!=table&&!Strings.isNullOrEmpty(table.value())){
            beanTable.setTableName(table.value());
        }else{
            beanTable.setTableName(StrFormat.camel2Underline(clazz.getSimpleName()));
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
                    BeanColumn beanColumn = new BeanColumn();
                    beanColumn.setField(field);
                    beanColumn.setFieldName(field.getName());
                    Column column = field.getAnnotation(Column.class);
                    if(null!=column&&!Strings.isNullOrEmpty(column.value())){
                        beanColumn.setColumnName(column.value());
                    }else{
                         beanColumn.setColumnName(field.getName());
                    }
                    //是否添加了 @Key 注解
                    Key key = field.getAnnotation(Key.class);
                    if(null!=key){
                        beanColumn.setKeyColumn(true);
                        beanColumn.setAutoGen(key.generatorIfNotSet());
                    }
                    //是否添加了 @Version 注解
                    if(field.isAnnotationPresent(Version.class)){
                        beanColumn.setVersionColumn(true);
                    }
                    beanTable.addBeanColumn(beanColumn);
                }
            }
        }
        return beanTable;
    }


}
