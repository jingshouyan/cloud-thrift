package com.jing.cloud.service.util.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 29017 on 2017/8/14.
 */
@ToString
@AllArgsConstructor()
public class OrderBy {
    @Getter
    private String key;
    @Getter
    private boolean asc;


    public static OrderBy newInstance(String key){
        return newInstance(key,true);
    }

    public static OrderBy newInstance(String key,boolean asc){
        return new OrderBy(key,asc);
    }
}
