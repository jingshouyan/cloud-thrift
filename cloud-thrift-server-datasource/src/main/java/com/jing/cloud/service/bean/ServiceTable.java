package com.jing.cloud.service.bean;

import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/8/2.
 */
@Data
@ToString(callSuper = true)
public class ServiceTable extends BaseBean{
    private Long serviceId;
    private String logicName;
    private String actualName;
    private String tableShardingExpression;
    private String datasourceShardingExpression;
}
