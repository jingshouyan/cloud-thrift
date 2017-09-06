package com.jing.cloud.service.method;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.bean.User;
import com.jing.cloud.util.RspUtil;

@Component
public class AddUser extends AbstractMethod<User>{

//	@Override
//	public Rsp valid(User u) {
//	    
//		return RspUtil.success();
//	}

	@Override
	public Rsp call(User u) throws Exception{
//	    try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
		System.out.println(Thread.currentThread().getName()+":"+JSON.toJSONString(u));
		return RspUtil.success(u);
	}

}
