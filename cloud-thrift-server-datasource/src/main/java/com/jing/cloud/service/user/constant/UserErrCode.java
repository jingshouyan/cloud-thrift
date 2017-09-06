package com.jing.cloud.service.user.constant;

import com.jing.cloud.util.ErrCode;
import org.springframework.stereotype.Component;

/**
 * Created by 29017 on 2017/9/4.
 */
@Component
public class UserErrCode {
    public static final int ACCOUNT_EXIST = 100100001;
    public static final int ACCOUNT_NOT_FOUND = 100100002;
    public static final int PASSWORD_NOT_SET = 100100003;
    public static final int PASSWORD_INVALID = 100100004;
    static{
        ErrCode.registerErrCode(ACCOUNT_EXIST,"account already exist.");
        ErrCode.registerErrCode(ACCOUNT_NOT_FOUND,"account not found.");
        ErrCode.registerErrCode(PASSWORD_NOT_SET,"password not set.");
        ErrCode.registerErrCode(PASSWORD_INVALID,"password invalid.");
    }
}
