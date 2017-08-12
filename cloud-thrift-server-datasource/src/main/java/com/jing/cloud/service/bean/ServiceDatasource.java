package com.jing.cloud.service.bean;

import com.jing.cloud.service.util.db.Column;
import com.jing.cloud.service.util.db.Table;
import com.jing.cloud.service.util.db.Version;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/7/29.
 */
@Data
@ToString(callSuper = true)
@Table("SERVICE_DATASOURCE")
public class ServiceDatasource extends BaseBean{

    @Column("NAME")
    private String name;
    private String driver;
    private String url;
    private String username;
    private String pwd;
    private Long serviceId;

}
