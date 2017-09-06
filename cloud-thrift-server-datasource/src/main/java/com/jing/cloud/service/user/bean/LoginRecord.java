package com.jing.cloud.service.user.bean;

import com.jing.cloud.service.bean.BaseBean;
import com.jing.cloud.service.util.db.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by 29017 on 2017/9/6.
 */
@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginRecord extends BaseBean {
    @Column(index = true)
    private Long ticketId;
    @Column(index = true)
    private Long userId;
    private Integer deviceType;
    private String deviceInfo;
    private String lang;
    private String mac;
    private Long loginAt;
    private Long logoutAt;
    private String logoutType;
}
