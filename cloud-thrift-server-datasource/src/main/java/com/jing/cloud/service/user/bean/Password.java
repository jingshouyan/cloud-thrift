package com.jing.cloud.service.user.bean;

import com.jing.cloud.service.bean.BaseBean;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/9/4.
 */
@Data
@ToString(callSuper = true)
public class Password extends BaseBean {
    private Long userId;
    private String password;
    private Integer wrongNumber;
    private Long frozenBefore;
    private String resetToken;
}
