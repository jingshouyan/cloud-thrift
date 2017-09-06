package com.jing.cloud.service.user.method.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by 29017 on 2017/9/5.
 */
@Data
public class LoginBean {
    @NotNull
    private String account;
    @NotNull
    private Integer accountType;
    @NotNull
    private String pw;
    @NotNull
    private Integer deviceType;
    @NotNull
    private String deviceInfo;
    @NotNull
    private String lang;
    @NotNull
    private String mac;
}
