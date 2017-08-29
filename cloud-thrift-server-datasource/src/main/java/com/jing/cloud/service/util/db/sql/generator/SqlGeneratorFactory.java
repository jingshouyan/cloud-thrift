package com.jing.cloud.service.util.db.sql.generator;

import lombok.Setter;

/**
 * Created by 29017 on 2017/8/29.
 */
public class SqlGeneratorFactory {
    public static final String MYSQL = "mysql";
    public static final String ORACLE = "oracle";
    public static final String MSSQL = "mssql";

    @Setter
    private static String databaseId = MYSQL;

    public static <T> SqlGenerator<T> getSqlGenerator(Class<T> clazz){
        switch (databaseId.toLowerCase()){
            case MYSQL:
                return new SqlGenerator4Mysql<>(clazz);
            case MSSQL:
            case ORACLE:

                default:
                    throw new IllegalArgumentException("Unsupported  database:" + databaseId);
        }
    }
}
