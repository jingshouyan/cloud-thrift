package com.jing.cloud.client.config;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jing.cloud.service.MicroService.Client;

public class ClientFactory implements PooledObjectFactory<Client>{
    
    private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    private String host;
    private int port;
    
    
    public ClientFactory(String host,int port) {
        this.host = host;
        this.port = port;
    }
    
    @Override
    public PooledObject<Client> makeObject() throws Exception {
        TSocket transport2 = new TSocket(host, port,5000);
        TTransport transport = new TFramedTransport(transport2);
        try{
            
            transport.open();
        }catch(TTransportException e){
            logger.error("create thrift client failed [host:{},port:{}]",host,port,e);
            transport.close();
            throw e;
        }
        Client client = new Client(new TBinaryProtocol(transport));
        logger.debug("create thrift client success [host:{},port:{}]",host,port);
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<Client> p) throws Exception {
        Client client = p.getObject();
        if(client!=null){
            client = null;
        }
    }

    @Override
    public boolean validateObject(PooledObject<Client> p) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void activateObject(PooledObject<Client> p) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void passivateObject(PooledObject<Client> p) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
}
