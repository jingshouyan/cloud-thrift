package com.jing.cloud.service.thrift;

import com.jing.cloud.service.config.ServConf;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.MDC;

import java.net.Socket;

/**
 * Created by 29017 on 2017/8/2.
 */
public class TFramedTransportFactory  extends TFastFramedTransport.Factory{

    @Override
    public TTransport getTransport(TTransport trans) {
        System.out.println(trans);
        if(trans instanceof TSocket){

            TSocket tsocket = (TSocket) trans;
            Socket socket = tsocket.getSocket();
            if(null!=socket){
                String clientIp = String.valueOf(socket.getRemoteSocketAddress());
                System.out.println(clientIp);
                MDC.put(ServConf.CLIENT_IP,clientIp);
            }
        }

        return new TFastFramedTransport(trans){

            @Override
            public void close(){
                MDC.remove(ServConf.CLIENT_IP);
                super.close();
            }
        };
    }


}
