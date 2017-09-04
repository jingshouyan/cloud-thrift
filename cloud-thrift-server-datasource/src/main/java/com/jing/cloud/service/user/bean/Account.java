package com.jing.cloud.service.user.bean;

import com.jing.cloud.service.bean.BaseBean;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/9/4.
 */
@Data
@ToString(callSuper = true)
public class Account extends BaseBean {
    private Long userId;
    private String account;
    private Integer type;
}
