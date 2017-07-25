package com.jing.cloud.client.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jing.cloud.service.ServiceInfo;

public class Node {
    private String key;
    private String version;
    private String path;
    private ServiceInfo info;
    private Map<String,Node> children = new ConcurrentHashMap<>();
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
       
    public Map<String, Node> getChildren() {
        return children;
    }
    
    public void setChildren(Map<String, Node> children) {
        this.children = children;
    }

    
    public String getVersion() {
        return version;
    }

    
    public void setVersion(String version) {
        this.version = version;
    }

    
    public ServiceInfo getInfo() {
        return info;
    }

    
    public void setInfo(ServiceInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Node [key=" + key + ", version=" + version + ", path=" + path + ", info=" + info + ", children="
                + children + "]";
    }

    
    public String getPath() {
        return path;
    }

    
    public void setPath(String path) {
        this.path = path;
    }
}
