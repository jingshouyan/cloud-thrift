package com.jing.cloud.service.user.bean;

import com.jing.cloud.service.bean.BaseBean;
import com.jing.cloud.service.user.constant.UserConstant;
import com.jing.cloud.service.util.db.annotation.Column;
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
    @Column(index = true)
    private Long userId;
    private Integer deviceType;
    private String deviceInfo;
    private String lang;
    private String mac;
    private Integer socketConnect = UserConstant.SOCKET_UNCONNECT;//默认未链接，可以考虑放在初始化时赋值
    private String pushSign;
    private String pushToken;
}
