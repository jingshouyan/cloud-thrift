package com.jing.cloud.service.bean;

import com.jing.cloud.service.util.db.annotation.Column;
import com.jing.cloud.service.util.db.annotation.Table;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/8/2.
 */
@Data
@ToString(callSuper = true)
@Table("ser_table_test")
public class ServiceTable extends BaseBean{
    private Long serviceId;
    @Column(length = 200000,encrypt = true,encryptKey = "id",value = "table_logic_name")
    private String logicName;
    private Integer tableSharding;
    private String tableShardingKey;
    private Integer dataSourceSharding;
    private String dataSourceShardingKey;
}
