package com.jing.cloud.client.config;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jing.cloud.service.MicroService.Client;
import com.jing.cloud.service.Req;
import com.jing.cloud.service.Rsp;

public class ClientProxy {

    private static final Logger logger = LoggerFactory.getLogger(ClientProxy.class);
    private static final ZooKeeperListener listener = ZooKeeperListener.getInstance();
    private static final ServiceTree st = ServiceTree.getInstance();
 
    public static void main(String[] args) throws Exception{
        int corePoolSize =24;
        int maximumPoolSize =24;
        long keepAliveTime=0;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();;
        RejectedExecutionHandler  handler = new ThreadPoolExecutor.CallerRunsPolicy();
        ExecutorService executor = new ThreadPoolExecutor
                (corePoolSize,maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, handler);
        Req req = new Req();
        req.setServiceName("userService");
        req.setMethodName("addUser");
        req.setJsonParam("{'username':'jing','password':'123'}");
        Random r = new Random();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) {
            if(i%2==0){
                
                req.setMethodName("updateUser");
            } else {
                req.setMethodName("addUser");
            }
            logger.info(""+i);
//            executor.execute(new Runnable() {
//                
//                @Override
//                public void run() {
//                   
//                }
//            });
            // TODO Auto-generated method stub
                Rsp rsp = callMethod(req);
                logger.info(rsp.toString());

        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


    public static Rsp callMethod(Req req) throws Exception {
        logger.debug("callMethod req:{}",req);
        ZooKeeperListener.getInstance();
        ClientPool pool = st.getPool(req);
        Client client = pool.getClient();
        Rsp rsp = null;
        try{
            rsp = client.callMethod(req);
            pool.returnClient(client);
        }catch(TTransportException e){
            logger.error("callMethod req:{} error",req,e);
            
            pool.returnClient(client);
        }catch(Exception e){
            logger.error("callMethod req:{} error",req,e);
            pool.invalidateClient(client);
        }
        logger.debug("callMethod req:{} rsp:{}",req,rsp);
        return rsp;
    }

    public void callMethodOneway(Req req) throws TException {

    }

}
