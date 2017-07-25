package com.jing.cloud.client.config;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.jing.cloud.service.MicroService.Client;

public class ClientPool implements Closeable{
    private String serviceName;
    private String version;
    private String ssid;
    private String instanceName;
    private GenericObjectPool<Client> innerPool;
    
    public ClientPool(){}
    
    public void initPool(PooledObjectFactory<Client> factory,final GenericObjectPoolConfig conf){
        if(innerPool!=null){
            innerPool.close();
        }
        innerPool = new GenericObjectPool<>(factory,conf);
    }

    public Client getClient() throws Exception{
        return innerPool.borrowObject();
    }
    
    public void returnClient(Client client){
        if(client==null){
            return;
        }
        innerPool.returnObject(client);
    }
    
    public void invalidateClient(Client client) throws Exception{
        innerPool.invalidateObject(client);
    }
    
    @Override
    public void close() throws IOException {
        innerPool.close();
    }
    
 
    public String getVersion() {
        return version;
    }

    
    public ClientPool setVersion(String version) {
        this.version = version;
        return this;
    }

    
    public String getSsid() {
        return ssid;
    }

    
    public ClientPool setSsid(String ssid) {
        this.ssid = ssid;
        return this;
    }

    
    public String getServiceName() {
        return serviceName;
    }

    
    public ClientPool setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    
    public String getInstanceName() {
        return instanceName;
    }

    
    public ClientPool setInstanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }


}
