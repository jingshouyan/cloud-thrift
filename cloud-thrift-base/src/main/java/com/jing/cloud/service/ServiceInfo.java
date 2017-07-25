package com.jing.cloud.service;


public class ServiceInfo {
    private String serviceName;
    private String version;
    private String host;
    private int port;
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        if(version==null)
            this.version = null;
        else
            this.version = version.toUpperCase(); 
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServiceInfo [serviceName=" + serviceName + ", version=" + version + ", host=" + host + ", port=" + port
                + "]";
    }
    
}
