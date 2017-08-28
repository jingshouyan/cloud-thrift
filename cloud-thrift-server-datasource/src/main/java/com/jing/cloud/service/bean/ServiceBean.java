package com.jing.cloud.service.bean;

import com.jing.cloud.service.util.db.annotation.Column;
import com.jing.cloud.service.util.db.annotation.Ignore;
import com.jing.cloud.service.util.db.annotation.Table;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by 29017 on 2017/8/10.
 */
@Data
@ToString(callSuper = true)
@Table("SERVICE_INFO_P")
public class ServiceBean extends BaseBean{
    @Column("SERVICE_NAME")
    private String serviceName;
    @Column("VERSION_V")
    private String version;

    private Boolean shardingShowSql;

    private Boolean shardingMetricsEnable;

    private Long shardingmetricsMillisPeriod;
    @Ignore
    private List<ServiceDatasource> ds;
    @Ignore
    private List<ServiceTable> tables;
}
