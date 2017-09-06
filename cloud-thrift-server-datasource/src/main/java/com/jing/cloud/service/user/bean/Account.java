package com.jing.cloud.service.user.bean;

import com.jing.cloud.service.bean.BaseBean;
import com.jing.cloud.service.util.db.annotation.Column;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/9/4.
 */
@Data
@ToString(callSuper = true)
public class Account extends BaseBean {
    @Column(index = true)
    private Long userId;
    @Column(index = true)
    private String account;
    private Integer type;
}
