package com.jing.cloud.service.bean;

import com.jing.cloud.service.util.db.annotation.Column;
import com.jing.cloud.service.util.db.annotation.Key;
import com.jing.cloud.service.util.db.annotation.Table;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/8/22.
 */
@Data
@ToString(callSuper = true)
@Table("im_user")
public class User{
    @Key
    private Long userID;
    private String name;
    private String realname;
    private String entExtend;
    private String school;
}
