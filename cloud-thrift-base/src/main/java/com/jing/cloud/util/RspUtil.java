package com.jing.cloud.util;

import com.alibaba.fastjson.JSON;
import com.jing.cloud.service.Rsp;

/**
 * 
 * @ClassName RspUtil
 * @Description 返回对象Rsp工具类
 * @author jingshouyan 290173092@qq.com
 * @Date 2017年6月6日 上午9:45:37
 * @version 1.0.0
 */
public class RspUtil {
    
    public static Rsp success(){
        return success(null);
    }
    
	public static Rsp success(Object result){
		return error(ErrCode.SUCCESS,result,null);
	}
	
	public static Rsp error(int errCode){
		return error(errCode,null,null);
	}
	
	public static Rsp error(int errCode,Throwable e){
		return error(errCode,null,e);
	}
	
	public static Rsp error(int errCode,Object result){
		return error(errCode,result,null);
	}
	
	/**
	 * 
	 * @Description 生成Rsp对象
	 * @param errCode 错误码
	 * @param result 返回对象
	 * @param e 异常信息
	 * @return Rsp对象  msg根据errCode对应的消息 result json序列化
	 */
	public static Rsp error(int errCode,Object result,Throwable e){
		Rsp rsp = new Rsp();
		rsp.setCode(errCode);
		if(null!=result){
			String jsonResult = JSON.toJSONString(result);
			rsp.setJsonResult(jsonResult);
		}
		String message = ErrCode.getMessage(errCode);
		if(null!=e){
			message = message+"|"+e.getMessage();
		}
		rsp.setMsg(message);
		return rsp;
	}
	
	public static Rsp error(int errCode,String message){
	    Rsp rsp = new Rsp();
	    rsp.setCode(errCode);
	    rsp.setMsg(message);
	    return rsp;
	}
}
