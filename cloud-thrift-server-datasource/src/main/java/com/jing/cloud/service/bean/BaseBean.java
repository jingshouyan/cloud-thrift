package com.jing.cloud.service.bean;

import com.jing.cloud.service.util.db.Key;
import lombok.Data;
import lombok.ToString;

/**
 * Created by 29017 on 2017/7/29.
 */
@Data
@ToString
public abstract class BaseBean {


    @Key
    private Long id;



    private Long createdAt;
    private Long updatedAt;
    private Long deletedAt;


    public void forCreate(){
        long now = System.currentTimeMillis();
        createdAt = now;
        updatedAt = now;
        deletedAt = null;
    }

    public void forUpdate(){
        long now = System.currentTimeMillis();
        createdAt = null;
        updatedAt = now;
        deletedAt = null;
    }

    public void forDelete(){
        long now = System.currentTimeMillis();
        createdAt = null;
        updatedAt = now;
        deletedAt = now;
    }

}
