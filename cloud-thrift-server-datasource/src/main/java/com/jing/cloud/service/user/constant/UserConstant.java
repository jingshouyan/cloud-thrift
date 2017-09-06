package com.jing.cloud.service.user.constant;

/**
 * Created by 29017 on 2017/9/4.
 */
public class UserConstant {
    public static final int USER_STATE_OK = 1;
    public static final int SOCKET_UNCONNECT = 0;
    public static final int SOCKET_CONNECT = 1;

    public static final int PW_WRONG_TIME_LOCK = 5;
    public static final long PW_LOCK_MS = 30L*60L*1000L;

    public static final String LOGOUT_TYPE_KECK = "kick";
    public static final String LOGOUT_TYPE_LOGOUT = "logout";
}
