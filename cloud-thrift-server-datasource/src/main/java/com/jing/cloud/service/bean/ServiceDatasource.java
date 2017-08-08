package com.jing.cloud.service.bean;

import com.jing.cloud.service.util.db.Version;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/7/29.
 */
@Data
@ToString(callSuper = true)
public class ServiceDatasource extends BaseBean{

    private String driver;
    private String url;
    private String username;
    private String pwd;
    private String serverName;
    private String version;

    @Version
    private long v;

}
