package com.jing.cloud.service.util.db.sql.generator;

import com.jing.cloud.service.util.db.Page;
import com.jing.cloud.service.util.db.SqlPrepared;

import java.util.List;
import java.util.Map;

/**
 * Created by 29017 on 2017/8/28.
 */
public interface SqlGenerator<T> {
    SqlPrepared query(Map<String,Object> condition);
    SqlPrepared query(Map<String,Object> condition, Page<T> page);
    SqlPrepared count(Map<String,Object> condition);
    SqlPrepared insert(List<T> beans);
    SqlPrepared update(T bean, Map<String,Object> condition);
    SqlPrepared delete(Map<String,Object> condition);
    SqlPrepared createTableSql();
    SqlPrepared dropTableSql();
}
