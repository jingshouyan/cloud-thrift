package com.jing.cloud.client.config;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.jing.cloud.service.MicroService.Client;

public class PoolFactory {
    
    private static final int MAX_TOTAL = 20;
//    private static final int MIN_IDLE = 3;
    
    public static GenericObjectPool<Client> createPool(String host,int port){
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxTotal(MAX_TOTAL);
//        conf.setMinIdle(MIN_IDLE);
        
        GenericObjectPool<Client> pool = new GenericObjectPool<>(new ClientFactory(host,port),conf);
        return pool;
    }
}
