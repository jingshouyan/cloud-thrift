package com.jing.cloud.client.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jing.cloud.service.Conf;
import com.jing.cloud.service.ServiceInfo;
import com.jing.cloud.service.MicroService.Client;
import com.jing.cloud.service.Req;

public class ServiceTree {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceTree.class);
    private static final String SPLIT_STR = "/";
    private CountDownLatch latch = new CountDownLatch(1);
    
    private final Node root = new Node();
    private final Map<String,ClientPool> clientMap = new ConcurrentHashMap<>();
    
    private ServiceTree(){
        init();
    }
    
    private static ServiceTree instance = new ServiceTree();
    
    public static ServiceTree getInstance(){
        return instance;
    }

    /**
     * 数据同步完成后调用
     * @Description 
     */
    public void countDown(){
        latch.countDown();
    }


    /**
     * 注册服务节点
     * @Description 
     * @param path zooKeeper节点路径
     * @param info zooKeeper节点数据
     */
    public void register(String path, ServiceInfo info) {
        logger.info("register path:[{}] info:[{}]",path,info);
        String[] strs = path.split(SPLIT_STR);
        int len = strs.length;
        if (len < 3) {
            logger.warn("invalid path [{}]", path);
            return;
        }
        if (!strs[1].equals(root.getKey())) {
            logger.warn("invalid root [{}]", path);
            return;
        }
        Node node = root;
        // 以 2 级树 key 加锁
        synchronized (strs[2].intern()) {
            for (int i = 2; i < strs.length; i++) {
                String str = strs[i];
                Node currNode = node.getChildren().get(str);
                if (currNode == null) {
                    currNode = new Node();
                    currNode.setKey(str);
                    if (i == strs.length - 1 && info != null) {
                        currNode.setInfo(info);
                        currNode.setPath(path);
                        currNode.setVersion(info.getVersion());
                    }
                    node.getChildren().put(str, currNode);
                }
                node = currNode;
            }
        }
    }

    /**
     * 注销服务节点
     * @Description 
     * @param path 节点路径
     */
    public void unregister(String path) {
        logger.info("register path:[{}]",path);
        String[] strs = path.split(SPLIT_STR);
        int len = strs.length;
        if (len < 4) {
            // 只对4级节点进行移除
            logger.warn("invalid path [{}]", path);
            return;
        }
        if (!strs[1].equals(root.getKey())) {
            logger.warn("invalid root [{}]", path);
            return;
        }
        Node node = root;
        // 以 2 级树 key 加锁
        synchronized (strs[2].intern()) {
            for (int i = 2; i < strs.length; i++) {
                String str = strs[i];
                Node currNode = node.getChildren().get(str);
                if (currNode != null) {
                    if (i == strs.length - 1) {
                        Node n = node.getChildren().get(str);
                        //当节点移除时，关闭连接池
                        closePool(n.getKey());
                        node.getChildren().remove(str);
                    }
                    node = currNode;
                } else {
                    return;
                }
            }
        }

    }

    private Node getNode(ServiceInfo info){
        Node node = getNode2(info);
        logger.trace("getNode info:[{}],node:[{}]",info,node);
        return node;
    }
    
    private Node getNode2(final ServiceInfo info) {
        
        try {
            //等待同步完成
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Node service = root.getChildren().get(info.getServiceName());
        if (service == null || service.getChildren().isEmpty()) {
            return null;
        }
        Collection<Node> nodes = service.getChildren().values();
        // 指定版本号
        if (info.getVersion() != null) {
            logger.debug("filter version:[{}]",info.getVersion());
            nodes = Collections2.filter(nodes, new Predicate<Node>() {
                @Override
                public boolean apply(Node input) {
                    return info.getVersion().equals(input.getVersion());
                }
            });
        }

        if (nodes.isEmpty()) {
            return null;
        }
        if (nodes.size() == 1) {
            return nodes.iterator().next();
        }

        return getOne(info, nodes);
    }

    private static Node getOne(ServiceInfo info, Collection<Node> nodes) {
        Random r = new Random();
        int j = r.nextInt(nodes.size());
        Iterator<Node> iterator = nodes.iterator();
        for (int i = 0; i < j; i++) {
            iterator.next();
        }
        return iterator.next();
    }
  

    private void init() {
        root.setKey(Conf.SERVICE_ROOT);
    }
    
    private ClientPool getClientPool(Node node){
        synchronized (node.getKey().intern()) {
            ClientPool pool = clientMap.get(node.getKey());
            if(pool==null){
                pool = createClientPool(node.getInfo());
                clientMap.put(node.getKey(), pool);
            }
            return pool;
        }
    }
    
    private void closePool(String key){
        ClientPool pool = clientMap.get(key);
        if(pool!=null){
            try {
                pool.close();
            } catch (IOException e) {
                logger.warn("close pool error key:[{}]",key,e);
            }
        }
    }
    
    

    private ClientPool createClientPool(ServiceInfo info) {
        logger.info("create pool [{}]", info);
        String host = info.getHost();
        int port = info.getPort();
        ClientPool pool = new ClientPool();
        PooledObjectFactory<Client> factory = new ClientFactory(host, port);
        GenericObjectPoolConfig conf = new GenericObjectPoolConfig();
        conf.setMaxTotal(ClientConf.MAX_TOTAL);
        pool.setServiceName(info.getServiceName()).initPool(factory, conf);
        return pool;
    }
    
    
    public ClientPool getPool(Req req){
        ServiceInfo info = new ServiceInfo();
        info.setServiceName(req.getServiceName());
        info.setVersion(req.getVersion());
        Node node = getNode(info);
        if(null == node){
            return null;
        }
        return getClientPool(node);
    }

    public static void main(String[] args) {
        ServiceTree t = ServiceTree.getInstance();
        String path = "";
        for(int i = 0;i<100;i++){
            ServiceInfo info = new ServiceInfo();
            path = "/microService/userService/userService-"+i;
            info.setServiceName("userService");
            info.setVersion("v1");
            info.setHost("127.0.0.1");
            info.setPort(1547+i);
            t.register(path, info);
        }       
        System.out.println(t.root);
        for(int i=0;i<10;i++){
            t.countDown();
            t.countDown();
            ServiceInfo info = new ServiceInfo();
            info.setServiceName("userService");
            info.setVersion("v1");
            Node node = t.getNode(info);
            System.out.println(node);
        }
        long start = System.currentTimeMillis();
//        for(int i=0;i<1000;i++){
//            ServiceInfo info = new ServiceInfo();
//            info.setServiceName("userService");
//            info.setVersion("v1");
//            Node node = t.getNode(info);
//        }
        long end = System.currentTimeMillis();
        
        System.out.println(end-start);
       
        t.unregister(path);
        

    }
}
