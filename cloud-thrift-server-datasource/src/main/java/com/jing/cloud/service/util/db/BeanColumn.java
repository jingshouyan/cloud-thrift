package com.jing.cloud.service.util.db;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * Created by 29017 on 2017/8/10.
 */
@Data
public class BeanColumn {
    private Field field;
    private String fieldName;
    private String columnName;
    private int columnLength;
    private String columnType;
    private boolean encrypt = false;
    private String pwdField;
    private boolean keyColumn = false;
    private boolean autoGen = false;
    private boolean index = false;
}
