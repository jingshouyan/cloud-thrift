package com.jing.cloud.service.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetUtil {
    
    private static Map<String,String> ips = new ConcurrentHashMap<>();
    static{
        try {
            init();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static boolean isLocalPortUsing(int port){
        String host = "127.0.0.1";
        return isPortUsing(host,port);
    }
    
    public static boolean isPortUsing(String host,int port){
        boolean flag = false;
        try{
            Socket socket = new Socket(host,port);
            socket.close();
            flag = true;
            
        }catch(Exception e){}
        return flag;
    }
    
    
    
    public static String getIp(String netSegment){
        String key = netSegment.substring(0, netSegment.lastIndexOf("."));
        return ips.get(key);
    }
    
    public static void main(String[] args) {
        boolean lpu = isLocalPortUsing(6379);
        System.out.println(lpu);
    }
    
    private static void init() throws SocketException{
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while(networkInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while(addresses.hasMoreElements()){
                InetAddress address = addresses.nextElement();
                if(null!=address&&address instanceof Inet4Address){
                    String addr = address.getHostAddress();
                    String key = addr.substring(0, addr.lastIndexOf("."));
                    ips.put(key, addr);
                }
                
            }
        }
    }
}
