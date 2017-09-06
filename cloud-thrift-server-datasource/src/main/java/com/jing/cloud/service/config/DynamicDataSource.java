package com.jing.cloud.service.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.config.ShardingPropertiesConstant;
import com.dangdang.ddframe.rdb.sharding.jdbc.core.datasource.ShardingDataSource;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jing.cloud.service.aligorithm.ModuloShardingAlgorithm;
import com.jing.cloud.service.bean.ServiceBean;
import com.jing.cloud.service.bean.ServiceDatasource;
import com.jing.cloud.service.bean.ServiceTable;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by 29017 on 2017/8/18.
 */
public class DynamicDataSource {


    @SneakyThrows
    public static ShardingDataSource shardingDataSource(ServiceBean sb){
        List<ServiceDatasource> ds = sb.getDs();
        Map<String,DataSource> dsMap = Maps.newHashMap();
        for(ServiceDatasource d:ds){
            dsMap.put(d.getName(),dataSource(d));
        }
        DataSourceRule dataSourceRule = new DataSourceRule(dsMap,ds.get(0).getName());
        List<ServiceTable> ts = sb.getTables();
        List<TableRule> tableRules = Lists.newArrayList();
        for(ServiceTable table:ts){
            List<String> actualTables = actualNames(table.getLogicName(),table.getTableSharding());
            TableRule tableRule = TableRule.builder(table.getLogicName())
                    .actualTables(actualTables)
                    .dataSourceRule(dataSourceRule)
                    .databaseShardingStrategy(new DatabaseShardingStrategy(table.getDataSourceShardingKey(),new ModuloShardingAlgorithm()))
                    .tableShardingStrategy(new TableShardingStrategy(table.getTableShardingKey(),new ModuloShardingAlgorithm()))
                    .build();
            tableRules.add(tableRule);
        }
        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(tableRules)
                .build();

        Properties properties = new Properties();
        if(null!=sb.getShardingShowSql()){
            properties.put(ShardingPropertiesConstant.SQL_SHOW.getKey(),sb.getShardingShowSql().toString());
        }

        ShardingDataSource shardingDataSource=  new ShardingDataSource(shardingRule,properties);

        return shardingDataSource;
    }

    private static List<String> actualNames(String logicName,int sharding){
        List<String> actualNames = Lists.newArrayList();
        for (int i = 0; i < sharding; i++) {
            String actualName = logicName+String.format("_%02d",i);
            actualNames.add(actualName);
        }
        return actualNames;
    }


    public static DataSource dataSource(ServiceDatasource ds){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(ds.getDriver());
        dataSource.setUrl(ds.getUrl());
        dataSource.setUsername(ds.getUsername());
        dataSource.setPassword(ds.getPwd());
        dataSource.setTestWhileIdle(ds.getTestWhileIdle());
        dataSource.setValidationQuery(ds.getValidationQuery());
        dataSource.setInitialSize(ds.getInitialSize());
        dataSource.setMinIdle(ds.getMinIdle());
        dataSource.setMaxActive(ds.getMaxActive());
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

}
