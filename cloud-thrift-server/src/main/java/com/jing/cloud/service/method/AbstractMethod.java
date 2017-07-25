package com.jing.cloud.service.method;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.jing.cloud.service.Rsp;
import com.jing.cloud.util.ErrCode;
import com.jing.cloud.util.RspUtil;

public abstract class AbstractMethod<T> implements Method<T>{

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    
    /**
     * 子类调用时获取父类中的泛型
     * @thx 江南白衣
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getClazz() {
        Class<T> clazz =null;
            Type t = getClass().getGenericSuperclass();
            if(t instanceof ParameterizedType){
                Type[] p = ((ParameterizedType)t).getActualTypeArguments();
                clazz = (Class<T>)p[0];
            }
        
        return clazz;
    }

    @Override
    public Rsp valid(T t) {
        Rsp rsp;
        Set<ConstraintViolation<T>> cvs = validator.validate(t);
        StringBuilder sb = new StringBuilder();
        for(ConstraintViolation<T> cv :cvs){
            sb.append(cv.getPropertyPath().toString());
            sb.append(" ");
            sb.append(cv.getMessage());
            sb.append("\t");
        }
        if(cvs.isEmpty()){
            rsp = RspUtil.success();
        }else{
            rsp = RspUtil.error(ErrCode.PARAM_INVALID,sb.toString());
        }
        return rsp;
    }

}
