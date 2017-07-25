package com.jing.cloud.service.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jing.cloud.service.MicroService.Iface;
import com.jing.cloud.service.MicroService.Processor;

public class ThreadPoolServer {
    
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolServer.class);

    public static TServer getServer(Iface service,int port){
        logger.info("thrift service starting...[port:{}]",port);
        TServer server = null;
        try {
            Processor<Iface> processor = new Processor<Iface>(service);
            TServerSocket transport = new TServerSocket(port);
            Args tArgs = new Args(transport);
            tArgs.processor(processor);
            tArgs.protocolFactory(new TCompactProtocol.Factory());
            tArgs.transportFactory(new TFramedTransport.Factory());
            server = new TThreadPoolServer(tArgs);
        } catch (TTransportException e) {
            logger.error("thrift service start failed",e);
        }
        logger.info("thrift service started.  [port:{}]",port);
        return server;
    }
}
