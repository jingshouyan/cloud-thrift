package com.jing.cloud.service.method;

import com.jing.cloud.service.Rsp;

public interface Method<T> {
    Class<T> getClazz();
	Rsp valid(T t) throws Exception;
	Rsp call(T t) throws Exception;
}
