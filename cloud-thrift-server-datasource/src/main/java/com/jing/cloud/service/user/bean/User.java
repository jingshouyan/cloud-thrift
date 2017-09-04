package com.jing.cloud.service.user.bean;

import com.jing.cloud.service.bean.BaseBean;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/8/31.
 */
@Data
@ToString(callSuper = true)
public class User extends BaseBean {
    private String nickname;
    private String signature;
    private Integer sex;
    private Long birthDate;
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private Integer state;

}
