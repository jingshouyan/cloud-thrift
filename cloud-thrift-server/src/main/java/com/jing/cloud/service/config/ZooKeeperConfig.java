package com.jing.cloud.service.config;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.thrift.server.TServer;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.jing.cloud.service.Conf;
import com.jing.cloud.service.ServiceInfo;
import com.jing.cloud.service.impl.MicroServiceImpl;
import com.jing.cloud.service.thrift.ThreadSelectorServer;
import com.jing.cloud.service.util.NetUtil;

/**
 * 
 * @ClassName ZooKeeperConfig
 * @Description thrift服务启动，ZooKeeper注册
 * @author jingshouyan 290173092@qq.com
 * @Date 2017年7月13日 下午10:39:50
 * @version 1.0.0
 */
@Configuration
public class ZooKeeperConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperConfig.class);
    
    private static final int PORT_MIN=12000;
    private static final int PORT_MAX=13000;
    
    private String serviceName = ServConf.getString("service.name");
    private String version = ServConf.getString("service.version");
    
    @Autowired
    private MicroServiceImpl microServiceImpl;
    
    private String serviceIp = NetUtil.getIp(Conf.NET_SEGMENT);
    
    private String serverList=Conf.ZOOKEEPER_LIST;
    
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    
    @PostConstruct
    private void init(){
        final int port = getRandomPort();
        executor.execute(new Runnable() {           
            @Override
            public void run() {
                threadPoolServer(port).serve();
            }
        });
        
        executor.execute(new Runnable() {           
            @Override
            public void run() {
                try {
                    registerService(port);
                } catch (Exception e) {
                    logger.error("register to ZooKeeper faild",e);
                }
            }
        });
    }
    
    
    private void registerService(int port) throws Exception{
        logger.info("register zooKeeper starting...");
        CuratorFramework client = CuratorFrameworkFactory.newClient(serverList, new RetryNTimes(10, 5000));
        String servicePath = Conf.ZOOKEEPER_BASEPATH+serviceName;
        ServiceInfo info = new ServiceInfo();
        info.setServiceName(serviceName);
        info.setVersion(version);
        info.setHost(serviceIp);
        info.setPort(port);
        String serviceInstance = servicePath+"/"+info.getServiceName();
        String data = JSON.toJSONString(info);
        client.start();
        String realInstance = client.create().
            creatingParentContainersIfNeeded()
            .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
            .forPath(serviceInstance, data.getBytes());
        logger.info("register zooKeeper path:[{}] data:[{}]",realInstance,data);
    }

    
    
    /**
     * 
     * @Description 
     * @param port
     * @return
     */
    public TServer threadPoolServer(int port) {
        return ThreadSelectorServer.getServer(microServiceImpl, port);
    }
    
    private int getRandomPort(){
        while(true){
            Random r = new Random();
            int port = r.nextInt(PORT_MAX-PORT_MIN)+PORT_MIN;
            logger.info(""+port);
            if(!NetUtil.isLocalPortUsing(port)){
                return port;
            }
        }
    }
}
