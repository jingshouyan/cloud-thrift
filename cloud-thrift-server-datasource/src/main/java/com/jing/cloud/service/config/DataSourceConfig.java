package com.jing.cloud.service.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jing.cloud.service.bean.ServiceBean;
import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.bean.ServiceTable;
import com.jing.cloud.service.util.keygen.DefaultKeyGenerator;
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
    @Value("${db.maxActive:200}")
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

        ServiceBean serviceBean = serviceBean();


        DataSource dataSource =DynamicDataSource.shardingDataSource(serviceBean);
        return new NamedParameterJdbcTemplate(dataSource);
    }


    private ServiceBean serviceBean(){
        ServiceBean sbean = new ServiceBean();
        sbean.setServiceName("im");
        sbean.setVersion("1.0.0");
        sbean.setId(genKey());
        sbean.setShardingShowSql(false);
        sbean.setShardingMetricsEnable(false);
        sbean.setShardingmetricsMillisPeriod(100000L);
        sbean.forCreate();
        List<ServiceDatasource> ds = new ArrayList<>();
        sbean.setDs(ds);
        for (int i = 0; i < 2; i++) {
            String dsName = "user_0"+i;
            ServiceDatasource sds = new ServiceDatasource();
            sds.setId(genKey());
            sds.setServiceId(sbean.getId());
            sds.setDriver("com.mysql.jdbc.Driver");
            sds.setUrl("jdbc:mysql://127.0.0.1:3306/"+dsName+"?useUnicode=true&characterEncoding=utf8");
            sds.setName(dsName);
            sds.setUsername("jing");
            sds.setPwd("le");
            sds.setTestWhileIdle(true);
            sds.setValidationQuery("select 1");
            sds.setInitialSize(5);
            sds.setMinIdle(5);
            sds.setMaxActive(50);
            sds.forCreate();
            ds.add(sds);
        }

        List<ServiceTable> stables = new ArrayList<>();
        sbean.setTables(stables);
        ServiceTable ut = new ServiceTable();
        ut.setId(genKey());
        ut.setServiceId(sbean.getId());
        ut.setLogicName("user");
        ut.setTableShardingKey("id");
        ut.setTableSharding(5);
        ut.setDataSourceShardingKey("id");
        ut.setDataSourceSharding(2);
        ut.forCreate();
        stables.add(ut);

        ServiceTable at = new ServiceTable();
        at.setId(genKey());
        at.setServiceId(sbean.getId());
        at.setLogicName("account");
        at.setTableShardingKey("userId");
        at.setTableSharding(5);
        at.setDataSourceShardingKey("userId");
        at.setDataSourceSharding(2);
        at.forCreate();
        stables.add(at);

        ServiceTable pt = new ServiceTable();
        pt.setId(genKey());
        pt.setServiceId(sbean.getId());
        pt.setLogicName("password");
        pt.setTableShardingKey("userId");
        pt.setTableSharding(5);
        pt.setDataSourceShardingKey("userId");
        pt.setDataSourceSharding(2);
        pt.forCreate();
        stables.add(pt);
        ServiceTable tt = new ServiceTable();
        tt.setId(genKey());
        tt.setServiceId(sbean.getId());
        tt.setLogicName("ticket");
        tt.setTableShardingKey("userId");
        tt.setTableSharding(5);
        tt.setDataSourceShardingKey("userId");
        tt.setDataSourceSharding(2);
        tt.forCreate();
        stables.add(tt);

        return sbean;
    }

    private long genKey(){
        return DefaultKeyGenerator.getInstance().generateKey().longValue();
    }
}
