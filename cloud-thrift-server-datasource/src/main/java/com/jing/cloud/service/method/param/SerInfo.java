package com.jing.cloud.service.method.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by 29017 on 2017/8/17.
 */
@Data
public class SerInfo {
    @NotNull
    private String serviceName;
    @NotNull
    private String version;
}
