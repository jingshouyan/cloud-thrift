package com.jing.cloud.service.util.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 29017 on 2017/8/10.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String value() default "";
    int length() default 500;
    String type() default "";
    boolean encrypt() default false;
    String encryptKey() default "";
    boolean index() default false;
}
