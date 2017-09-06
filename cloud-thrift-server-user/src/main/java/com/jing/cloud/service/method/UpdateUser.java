package com.jing.cloud.service.method;

import org.springframework.stereotype.Component;

import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.bean.User;
import com.jing.cloud.util.RspUtil;

@Component
public class UpdateUser extends AbstractMethod<User>{



    @Override
    public Rsp call(User u) throws Exception{
//      try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        System.out.println(Thread.currentThread().getName()+":"+u);
        return RspUtil.success(u);
    }

}
