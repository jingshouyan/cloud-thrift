package com.jing.cloud.service.util.db.annotation;

import java.lang.annotation.*;

/**
 * Created by 29017 on 2017/8/8.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Version {
    String value() default "";
}
