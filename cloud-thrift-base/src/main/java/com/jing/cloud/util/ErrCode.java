package com.jing.cloud.util;

import java.util.HashMap;
import java.util.Map;

public class ErrCode {    
	public static final int SUCCESS=0;	
	public static final int SERVER_ERROR=99900001;
	public static final int METHOD_NOT_FOUND=99900002;
	public static final int JSON_PARSE_ERROR=99900003;
	public static final int PARAM_INVALID=99900004;
	private static final Map<Integer, String> CODE_MAP = new HashMap<>();
	static{
		CODE_MAP.put(SUCCESS, "success");
		CODE_MAP.put(SERVER_ERROR, "server error");
		CODE_MAP.put(METHOD_NOT_FOUND, "method not found");
		CODE_MAP.put(JSON_PARSE_ERROR, "json parse error");
		CODE_MAP.put(PARAM_INVALID, "param invalid");
	}
	
	public static String getMessage(int errCode){
		String message = CODE_MAP.get(errCode);
		if(null == message){
			message="this error code ["+ errCode +"] is undefined";
		}
		return message;
	}
	
	public static void registerErrCode(int errCode,String message){
	    String msg = CODE_MAP.get(errCode);
	    if(null!=msg){
	        
	    }
	    CODE_MAP.put(errCode, message);
	}
}
