package com.jing.cloud.service.user.bean;

import com.jing.cloud.service.bean.BaseBean;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by 29017 on 2017/9/5.
 */
@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Ticket extends BaseBean {
    private Long userId;
    private String ticket;
    private Integer deviceType;
    private String deviceInfo;
    private String lang;
    private String mac;

}
