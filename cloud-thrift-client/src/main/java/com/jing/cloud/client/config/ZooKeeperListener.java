package com.jing.cloud.client.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jing.cloud.service.Conf;
import com.jing.cloud.service.ServiceInfo;

public class ZooKeeperListener {
    private static final Logger logger = LoggerFactory.getLogger(ZooKeeperListener.class);
    private final CuratorFramework client = CuratorFrameworkFactory.
            newClient(Conf.ZOOKEEPER_LIST, new RetryNTimes(10, 5000));
    
    private static final ZooKeeperListener instance = new ZooKeeperListener();
    
    public static ZooKeeperListener getInstance(){
        return instance;
    }
    
    public CuratorFramework getClient(){
        return client;
    }
    
    private ZooKeeperListener(){
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void init() throws Exception{
        final ServiceTree st = ServiceTree.getInstance();
        client.start();
        @SuppressWarnings("resource")
        TreeCache cache = new TreeCache(client, "/"+Conf.SERVICE_ROOT);
        cache.start();
        TreeCacheListener listener = new TreeCacheListener() {
            
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                String type = event.getType().name();
                String path = null;
                String data = null;
                ChildData cd = event.getData();
                if(cd!=null){
                    path = event.getData().getPath();
                    data = str(event.getData().getData());
                }
                
                ServiceInfo info = toInfo(data);
                logger.info("tree changed type:[{}] path:[{}] data:[{}]",type,path,data);
                switch (event.getType()) {
                case NODE_ADDED:
                    st.register(path, info);
                    break;
                case NODE_UPDATED:
                    st.register(path, info);
                    break;
                case NODE_REMOVED:
                    st.unregister(path);
                    break;
                case INITIALIZED:
                    st.countDown();
                    break;
                default:
                    break;
                }
                
            }
        };
        cache.getListenable().addListener(listener);
    }
    
    private static ServiceInfo toInfo(String data){
        ServiceInfo info = null;
        if(null!=data&&!"".equals(data)){
            try{
                info = JSON.parseObject(data, ServiceInfo.class);
            }catch(Exception e){
                logger.warn("data:[{}] convert to ServiceInfo error",data,e);
            }
        }
        
        return info;
    }

    private static String str(byte[] b){
        if(b==null)
            return null;
        return new String(b);
    }
    
}
