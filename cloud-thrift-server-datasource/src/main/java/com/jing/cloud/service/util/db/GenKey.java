package com.jing.cloud.service.util.db;

import java.lang.annotation.*;

/**
 * Created by 29017 on 2017/8/4.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenKey {
}