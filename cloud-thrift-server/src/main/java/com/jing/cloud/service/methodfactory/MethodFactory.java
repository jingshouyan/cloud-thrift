package com.jing.cloud.service.methodfactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jing.cloud.service.method.Method;

public class MethodFactory {
	private static final Logger logger = LoggerFactory.getLogger(MethodFactory.class);
	private static final Map<String,Method> methodMap = new ConcurrentHashMap<>();
	
	public static Method getMethod(String methodName){
		return methodMap.get(methodName);
	}
	
	public static void register(Method method){
		String methodName = method.getClass().getSimpleName();
		methodName = lowerCaseIndex(methodName);
		methodMap.put(methodName, method);
		logger.info("method register [{} = {}]",methodName,method);
	}

	private static String lowerCaseIndex(String str){
	    if(str==null||str.length()==0){
	        return str;
	    }
	    String str2 = str.substring(0, 1).toLowerCase()+str.substring(1);
	    return str2;
	}
	
}
