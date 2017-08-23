package com.jing.cloud.service.config;

import java.sql.SQLException;
import javax.sql.DataSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jing.cloud.service.bean.ServiceBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;



@Configuration
@PropertySource("classpath:config/application-config.properties")
public class DataSourceConfig {

    @Value("${db.driver}")
    private String driver;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.testWhileIdle:false}")
    private boolean testWhileIdle;
    @Value("${db.validationQuery:SELECT 1}")
    private String validationQuery;
    @Value("${db.initialSize:10}")
    private int initialSize;
    @Value("${db.minIdle:5}")
    private int minIdle;
    @Value("${db.maxActive:100}")
    private int maxActive;

    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        try
        {
            dataSource.setFilters("stat,wall");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return dataSource;
    }

//    @Bean
//    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
//        return new NamedParameterJdbcTemplate(dataSource);
//    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        String json = "{\n" +
                "    \"createdAt\": 1503403632544,\n" +
                "    \"ds\": [\n" +
                "        {\n" +
                "            \"createdAt\": 1503403632611,\n" +
                "            \"driver\": \"com.mysql.jdbc.Driver\",\n" +
                "            \"id\": 106845836876447740,\n" +
                "            \"initialSize\": 5,\n" +
                "            \"maxActive\": 30,\n" +
                "            \"minIdle\": 5,\n" +
                "            \"name\": \"user_00\",\n" +
                "            \"pwd\": \"le\",\n" +
                "            \"serviceId\": 106845836599623680,\n" +
                "            \"testWhileIdle\": false,\n" +
                "            \"updatedAt\": 1503403632611,\n" +
                "            \"url\": \"jdbc:mysql://127.0.0.1:3306/user_00?useUnicode=true&characterEncoding=utf8\",\n" +
                "            \"username\": \"jing\",\n" +
                "            \"validationQuery\": \"select 1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"createdAt\": 1503403632643,\n" +
                "            \"driver\": \"com.mysql.jdbc.Driver\",\n" +
                "            \"id\": 106845837010665470,\n" +
                "            \"initialSize\": 5,\n" +
                "            \"maxActive\": 30,\n" +
                "            \"minIdle\": 5,\n" +
                "            \"name\": \"user_01\",\n" +
                "            \"pwd\": \"le\",\n" +
                "            \"serviceId\": 106845836599623680,\n" +
                "            \"testWhileIdle\": true,\n" +
                "            \"updatedAt\": 1503403632643,\n" +
                "            \"url\": \"jdbc:mysql://127.0.0.1:3306/user_01?useUnicode=true&characterEncoding=utf8\",\n" +
                "            \"username\": \"jing\",\n" +
                "            \"validationQuery\": \"select 1\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"id\": 106845836599623680,\n" +
                "    \"serviceName\": \"user\",\n" +
                "    \"tables\": [\n" +
                "        {\n" +
                "            \"createdAt\": 1503403632704,\n" +
                "            \"dataSourceSharding\": 2,\n" +
                "            \"dataSourceShardingKey\": \"sid\",\n" +
                "            \"id\": 106845837266518020,\n" +
                "            \"logicName\": \"user\",\n" +
                "            \"serviceId\": 106845836599623680,\n" +
                "            \"tableSharding\": 5,\n" +
                "            \"tableShardingKey\": \"sid\",\n" +
                "            \"updatedAt\": 1503403632704\n" +
                "        }\n" +
                "    ],\n" +
                "    \"updatedAt\": 1503403632544,\n" +
                "    \"version\": \"v1.0.0\"\n" +
                "}";
        ServiceBean serviceBean = JSONObject.parseObject(json,ServiceBean.class);
        DataSource dataSource =DynamicDataSource.shardingDataSource(serviceBean);
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
