package com.jing.cloud.service.thrift;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jing.cloud.service.MicroService.Iface;
import com.jing.cloud.service.MicroService.Processor;
import com.jing.cloud.service.config.ServConf;

public class ThreadSelectorServer {
    
    private static final Logger logger = LoggerFactory.getLogger(ThreadSelectorServer.class);
    
    private static final String SELECTOR_THREADS = "thrift.selectorThreads";
    private static final String WORKER_THREADS = "thrift.workerThreads";
    private static final int MAX_READ_BUFFER_BYTES = 25*1024*1024;
    
    public static TServer getServer(Iface service,int port) {
        
        int cpuNum = Runtime.getRuntime().availableProcessors();
        int selectorThreads = cpuNum*2;
        int workerThreads = cpuNum*4;
        if(null!=ServConf.getString(SELECTOR_THREADS)){
            selectorThreads = ServConf.getInt(SELECTOR_THREADS);
        }
        if(null!=ServConf.getString(WORKER_THREADS)){
            selectorThreads = ServConf.getInt(WORKER_THREADS);
        }
        
        
        
        TServer server =null;
        try{
            logger.info("thrift service starting...[port:{}]",port);
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);  
            //多线程半同步半异步  
            TThreadedSelectorServer.Args tArgs = new TThreadedSelectorServer.Args(serverTransport);  
            TProcessor tprocessor = new Processor<Iface>(service);
            tArgs.processor(tprocessor);  
            tArgs.transportFactory(new TFramedTransport.Factory());  
            tArgs.maxReadBufferBytes = MAX_READ_BUFFER_BYTES;//设置读的最大参数块 默认最大long，容易引起内存溢出，必须限制
            tArgs.selectorThreads(selectorThreads).workerThreads(workerThreads);
            //二进制协议  
            tArgs.protocolFactory(new TBinaryProtocol.Factory());  
            // 多线程半同步半异步的服务模型  
            server = new TThreadedSelectorServer(tArgs);  
        }catch(Exception e){
            logger.error("thrift service start failed",e);
        }
        logger.info("thrift service started.  [port:{}]",port);
        
        return server;
    }
}
