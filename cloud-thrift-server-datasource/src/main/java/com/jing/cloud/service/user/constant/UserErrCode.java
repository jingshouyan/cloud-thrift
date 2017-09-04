package com.jing.cloud.service.user.constant;

import com.jing.cloud.util.ErrCode;
import org.springframework.stereotype.Component;

/**
 * Created by 29017 on 2017/9/4.
 */
@Component
public class UserErrCode {
    public static final int ACCOUNT_EXIST = 100100001;

    static{
        ErrCode.registerErrCode(ACCOUNT_EXIST,"account already exist.");
    }
}
