package com.jing.cloud.service.util.db;

import lombok.Data;

import java.util.Map;

/**
 * Created by 29017 on 2017/8/28.
 */
@Data
public class SqlPrepared {
    private String sql;
    private Map<String,Object> params;
}
