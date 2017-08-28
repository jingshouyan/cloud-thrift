package com.jing.cloud.service.util.db;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

/**
 * Created by 29017 on 2017/8/10.
 */

@ToString
public class BeanTable {
    @Getter@Setter
    private Class<?> clazz;
    @Getter@Setter
    private String tableName;
    @Getter
    private BeanColumn key;
    @Getter
    private BeanColumn version;
    @Getter
    private Set<BeanColumn> columns = Sets.newHashSet();
    @Getter
    private Set<BeanColumn> encryptColumns = Sets.newHashSet();
    @Getter
    private Map<Field,BeanColumn> fieldMap = Maps.newHashMap();
    @Getter
    private Map<String,BeanColumn> fieldNameMap = Maps.newHashMap();
    @Getter
    private Map<String,BeanColumn> columnMap = Maps.newHashMap();

    /**
     * 添加列信息
     * @param column 列信息
     */
    public void addBeanColumn(BeanColumn column){
        columns.add(column);
        fieldMap.put(column.getField(),column);
        fieldNameMap.put(column.getFieldName(),column);
        columnMap.put(column.getColumnName(),column);
        //加密列
        if(column.isEncrypt()){
            encryptColumns.add(column);
        }
        if(column.isKeyColumn()&&key==null){
            //取第一个为 key
            //因为是先取 类 中的属性，然后再取 父类 中的属性
            key = column;
        }else if(column.isVersionColumn()&&column==null){
            //取第一个为 version
            version = column;
        }
    }

}
