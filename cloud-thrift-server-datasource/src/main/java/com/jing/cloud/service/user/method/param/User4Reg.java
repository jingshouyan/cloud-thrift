package com.jing.cloud.service.user.method.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by 29017 on 2017/9/4.
 */
@Data
public class User4Reg {
    @NotNull
    @Length(min = 2,max = 50)
    private String nickname;
    private int sex;
    private int accountType;
    @NotNull
    private String account;
    @NotNull
    private String password;
}
