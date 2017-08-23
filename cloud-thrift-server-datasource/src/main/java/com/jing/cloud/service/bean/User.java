package com.jing.cloud.service.bean;

import com.jing.cloud.service.util.db.annotation.Key;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/8/22.
 */
@Data
@ToString(callSuper = true)
public class User extends BaseBean{

    String sid;
    String username;
    String birthday;
}
