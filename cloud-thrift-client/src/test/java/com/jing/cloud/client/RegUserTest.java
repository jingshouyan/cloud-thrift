package com.jing.cloud.client;

import com.alibaba.fastjson.JSON;
import com.jing.cloud.client.config.ClientProxy;
import com.jing.cloud.service.MicroService;
import com.jing.cloud.service.Req;
import com.jing.cloud.service.Rsp;

import java.util.EmptyStackException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by 29017 on 2017/9/4.
 */
public class RegUserTest {

    private static Executor e = Executors.newFixedThreadPool(200);

    public static void main(String[] args) throws Exception {
        for (int i = 21000000; i <22000000 ; i++) {
            final int j = i;
            e.execute(new Runnable() {
                @Override
                public void run() {
                    Req req = new Req();
                    req.setServiceName("datasourceService");
                    req.setVersion("V1.0");
                    req.setMethodName("registerUser");
                    User4Reg u = new User4Reg();
                    u.setAccountType(1);
                    u.setAccount("bdc"+j);
                    u.setPassword("sdfkj");
                    u.setNickname("hehda");
                    u.setSex(1);
                    req.setJsonParam(JSON.toJSONString(u));
                    try{
                        Rsp rsp = ClientProxy.callMethod(req);
                        System.out.println(rsp);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

        }

    }
}
