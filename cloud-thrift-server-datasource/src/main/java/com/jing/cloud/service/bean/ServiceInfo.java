package com.jing.cloud.service.bean;

import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/8/10.
 */
@Data
@ToString(callSuper = true)
public class ServiceInfo extends BaseBean{
    private String serviceName;
    private String version;
}
