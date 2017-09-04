package com.jing.cloud.client;

import com.alibaba.fastjson.JSON;
import com.jing.cloud.client.config.ClientProxy;
import com.jing.cloud.service.MicroService;
import com.jing.cloud.service.Req;
import com.jing.cloud.service.Rsp;

/**
 * Created by 29017 on 2017/9/4.
 */
public class RegUserTest {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i <1000 ; i++) {
            Req req = new Req();
            req.setServiceName("datasourceService");
            req.setVersion("V1.0");
            req.setMethodName("registerUser");
            User4Reg u = new User4Reg();
            u.setAccountType(1);
            u.setAccount("abc127"+i);
            u.setPassword("sdfkj");
            u.setNickname("hehda");
            u.setSex(1);
            req.setJsonParam(JSON.toJSONString(u));
            Rsp rsp = ClientProxy.callMethod(req);
            System.out.println(rsp);
        }

    }
}
