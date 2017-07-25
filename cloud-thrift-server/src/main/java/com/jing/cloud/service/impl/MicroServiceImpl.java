package com.jing.cloud.service.impl;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jing.cloud.service.MicroService;
import com.jing.cloud.service.Req;
import com.jing.cloud.service.Rsp;
import com.jing.cloud.service.method.Method;
import com.jing.cloud.service.methodfactory.MethodFactory;
import com.jing.cloud.util.ErrCode;
import com.jing.cloud.util.RspUtil;

@Service
public class MicroServiceImpl implements MicroService.Iface {

    private static final Logger logger = LoggerFactory.getLogger(MicroServiceImpl.class);

    @Override
    public Rsp callMethod(Req req) throws TException {
        return call(req);
    }

    @Override
    public void callMethodOneway(Req req) throws TException {
        call(req);
    }

    private Rsp call(Req req) {
        logger.debug("call method start req:[{}]", req);
        logger.debug("call method start req:[{}]", JSON.toJSONString(req));
        Rsp rsp = null;
        try {
            @SuppressWarnings("unchecked")
            Method<Object> method = MethodFactory.getMethod(req.getMethodName());
            if (method == null) {
                // 方法没有找到，方法没有注册
                rsp = RspUtil.error(ErrCode.METHOD_NOT_FOUND);
            } else {
                Class<Object> clazz = method.getClazz();
                // json解析是否有成功
                boolean jsonParsed = false;
                // 暂定参数为json格式字符串
                Object jsonObject = null;
                try {
                    jsonObject = JSON.parseObject(req.jsonParam,clazz);
                    jsonParsed = true;// json 解析成功
                } catch (Exception e) {
                    // json 解析失败
                    rsp = RspUtil.error(ErrCode.JSON_PARSE_ERROR, e);
                    logger.error("call method json prase error req:[{}]", req, e);
                }
                if (jsonParsed) {

                    // 参数校验
                    rsp = method.valid(jsonObject);
                    if (ErrCode.SUCCESS == rsp.code) {
                        rsp = method.call(jsonObject);
                    }
                }
            }
        } catch (Exception e) {
            // 其他未处理的异常
            rsp = RspUtil.error(ErrCode.SERVER_ERROR, e);
            logger.error("call method error req:[{}]", req, e);
        }
        logger.debug("call method end req:[{}] rsp:[{}]", req, rsp);
        return rsp;
    }

}
