package com.jing.cloud.service.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 服务配置工具类
 * @ClassName ServConf
 * @Description 加载  classpath:config/config.properties 本jar包内配置文件
 *                     classpath:config/application-config.properties 服务jar包内配置文件
 * 资源文件到内存中
 * @notice key/value 进来行了 trim 处理
 * @author jingshouyan 290173092@qq.com
 * @Date 2017年7月13日 下午6:44:24
 * @version 1.0.0
 */
public class ServConf {

    public static final String CLIENT_IP = "clientIp";

    //因为配置项中包含了 logback 需要的环境变量，所以这里不能使用日志 
    //private static final Logger logger = LoggerFactory.getLogger(ServConf.class);
    private static final Map<String,String> cfg = new HashMap<>();
    private static final String CONFIG_FILEPATH = "/config/config.properties,/config/application-config.properties";
    private static final String SPLIT_STR = ",";
    static{
        String[] filepaths = CONFIG_FILEPATH.split(SPLIT_STR);
        for(String filepath: filepaths){
            loadConf(filepath);
        }
    }
    
    /**
     * 获取配置项
     * @Description 有可能是 null
     * @param key
     * @return 
     */
    public static String getString(String key){
        String value = cfg.get(key);
        if(value==null){
            System.err.println("config ["+key+"] is null");
        }
        return value;
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static int getInt(String key){
        String value = getString(key);
        return Integer.parseInt(value);
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static long getLong(String key){
        String value = getString(key);
        return Long.parseLong(value);
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static double getDouble(String key){
        String value = getString(key);
        return Double.parseDouble(value);
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static boolean getBoolean(String key){
        String value = getString(key);
        return Boolean.parseBoolean(value);
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static List<String> getStringList(String key){
        List<String> v = new ArrayList<>();
        String value = getString(key);
        String[] s = value.split(SPLIT_STR);
        for(int i=0;i<s.length;i++){
            v.add(s[i]);
        }
        return v;
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static List<Integer> getIntList(String key){
        List<Integer> v = new ArrayList<>();
        String value = getString(key);
        String[] s = value.split(SPLIT_STR);
        for(int i=0;i<s.length;i++){
            v.add(Integer.parseInt(s[i]));
        }
        return v;
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static List<Long> getLongList(String key){
        List<Long> v = new ArrayList<>();
        String value = getString(key);
        String[] s = value.split(SPLIT_STR);
        for(int i=0;i<s.length;i++){
            v.add(Long.parseLong(s[i]));
        }
        return v;
    }
    
    /**
     * 获取配置项
     * @Description 转换失败会抛出异常
     * @param key
     * @return
     */
    public static List<Double> getDoubleList(String key){
        List<Double> v = new ArrayList<>();
        String value = getString(key);
        String[] s = value.split(SPLIT_STR);
        for(int i=0;i<s.length;i++){
            v.add(Double.parseDouble(s[i]));
        }
        return v;
    }
    
    /**
     * 加载资源文件
     * @Description 
     */
    private static void loadConf(String configFilepath){
        Properties properties = new Properties();
        try{
            System.out.println("load config ["+configFilepath+"] start...");
            InputStream in = ServConf.class
                    .getResourceAsStream(configFilepath);
            properties.load(in);
            in.close();
            for(Object k:properties.keySet()){
                String key = String.valueOf(k).trim();
                String value = properties.getProperty(key).trim();
                cfg.put(key, value);
                System.out.println("load config  "+String.format("%1$-15s", key)+" = "+value);
            }
            System.out.println("load config ["+configFilepath+"] end");
        }catch(Exception e){
            System.err.println("load config ["+configFilepath+"] error");
            e.printStackTrace();
        }finally{
            
        }
    }
}
