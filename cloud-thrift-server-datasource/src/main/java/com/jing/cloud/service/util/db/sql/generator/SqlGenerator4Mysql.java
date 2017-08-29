package com.jing.cloud.service.util.db.sql.generator;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.jing.cloud.service.util.db.Bean4DbUtil;
import com.jing.cloud.service.util.db.BeanColumn;
import com.jing.cloud.service.util.db.BeanTable;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.Page;
import com.jing.cloud.service.util.db.SqlPrepared;
import lombok.NonNull;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** mysql
 * Created by 29017 on 2017/8/28.
 */
public class SqlGenerator4Mysql<T> implements SqlGenerator<T>{

    private Class<T> clazz;

    public SqlGenerator4Mysql(Class<T> clazz){
        this.clazz = clazz;
    }

    @Override
    public SqlPrepared query(Map<String,Object> condition){
        SqlPrepared sqlPrepared = new SqlPrepared();
        String sql = "SELECT * FROM " +tableName();
        SqlPrepared whereSql = where(condition);
        sqlPrepared.setSql(sql+whereSql.getSql());
        sqlPrepared.setParams(whereSql.getParams());
        return sqlPrepared;
    }

    @Override
    public SqlPrepared query(Map<String,Object> condition, Page<T> page){
        SqlPrepared sqlPrepared = new SqlPrepared();
        String sql = "SELECT * FROM " +tableName();
        SqlPrepared whereSql = where(condition);
        sql += whereSql.getSql();
        int offset = (page.getPage()-1)*page.getPageSize();
        sql += "LIMIT "+offset+","+page.getPageSize();
        sqlPrepared.setSql(sql);
        sqlPrepared.setParams(whereSql.getParams());
        return sqlPrepared;
    }
    @Override
    public SqlPrepared count(Map<String,Object> condition){
        SqlPrepared sqlPrepared = new SqlPrepared();
        String sql = "SELECT COUNT(*) FROM " +tableName();
        SqlPrepared whereSql = where(condition);
        sqlPrepared.setSql(sql+whereSql.getSql());
        sqlPrepared.setParams(whereSql.getParams());
        return sqlPrepared;
    }
    @Override
    public SqlPrepared batchInsert(List<T> beans){
        SqlPrepared sqlPrepared = new SqlPrepared();
        Object bean = beans.get(0);
        Class<?> clazz = bean.getClass();
        Map<String,Object> beanMap = Bean4DbUtil.Bean2Map(bean);
        StringBuilder sb = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        values.append(" VALUES ");
        int i=0;
        Map<Integer,String> keyMap = Maps.newHashMap();
        Map<String,Object> param = Maps.newHashMap();
        for(String key:beanMap.keySet()){
            // 空值不插入
            if (isEmtry(beanMap.get(key))) {
                continue;
            }
            //拼装keys字符串
            keys.append(columnName(key));
            keys.append(',');
            //将所有的key标上序号放在map中，待用
            keyMap.put(i, key);
            i++;
        }
        keys.deleteCharAt(keys.length()-1);
        int listSize = beans.size();
        for(int j=0;j<listSize;j++){
            bean = beans.get(j);
            beanMap = toMap(bean);
            StringBuilder oneValue = new StringBuilder();
            for(int k = 0;k<i;k++){
                String key = keyMap.get(k)+"_"+j+"_"+k;
                oneValue.append(':');
                oneValue.append(key);
                oneValue.append(',');
                param.put(key, beanMap.get(keyMap.get(k)));
            }
            oneValue.deleteCharAt(oneValue.length()-1);
            values.append("(");
            values.append(oneValue);
            values.append("),");
        }
        values.deleteCharAt(values.length()-1);
        sb.append("INSERT INTO ");
        sb.append(tableName());
        sb.append(" (");
        sb.append(keys);
        sb.append(") ");
        sb.append(values);
        sqlPrepared.setSql(sb.toString());
        sqlPrepared.setParams(param);
        return sqlPrepared;
    }

    @Override
    public SqlPrepared batchUpdate(T bean,Map<String,Object> condition){
        SqlPrepared sqlPrepared = new SqlPrepared();
        StringBuilder sql = new StringBuilder();
        Map<String,Object> beanMap = toMap(bean);
        Map<String,Object> param = Maps.newHashMap();
        sql.append("UPDATE ");
        sql.append(tableName());
        sql.append(" SET ");
        for(String key:beanMap.keySet()){
            Object value = beanMap.get(key);
            // 空 不更新
            if(isEmtry(value)){
                continue;
            }
            //主键不更新
            if(key.equals(key())){
                continue;
            }
            String column = columnName(key);
            sql.append(String.format(" %s = :%s__set ,",column,key));
            param.put(key+"__set",value);
        }
        //只有当有字段需要更新
        Preconditions.checkArgument(!param.isEmpty(),"nothing to update");
        //移除set 最后一个逗号 ,
        sql.deleteCharAt(sql.length()-1);
        SqlPrepared where = where(condition);
        sql.append(where.getSql());
        param.putAll(where.getParams());
        sqlPrepared.setSql(sql.toString());
        sqlPrepared.setParams(param);
        return sqlPrepared;
    }
    @Override
    public SqlPrepared delete(Map<String,Object> condition){
        SqlPrepared sqlPrepared = new SqlPrepared();
        String sql = "DELETE FROM "+tableName();
        SqlPrepared where = where(condition);
        sql +=where.getSql();
        sqlPrepared.setSql(sql);
        sqlPrepared.setParams(where.getParams());
        return sqlPrepared;
    }
    @Override
    public SqlPrepared createTableSql(){
        SqlPrepared sqlPrepared = new SqlPrepared();
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS `");
        sql.append(tableName());
        sql.append("` (");
        BeanTable beanTable = Bean4DbUtil.getBeanTable(clazz);
        for (BeanColumn column: beanTable.getColumns()) {
            sql.append(columnString(column));
            sql.append(" , ");
        }
        BeanColumn key = beanTable.getKey();
        if(null!=key){
            sql.append(" PRIMARY KEY (`");
            sql.append(key.getColumnName());
            sql.append("`)");
        }else{
            sql.deleteCharAt(sql.length()-1);
        }
        sql.append(");");
        sqlPrepared.setSql(sql.toString());
        return sqlPrepared;
    }

    @Override
    public SqlPrepared dropTableSql(){
        SqlPrepared sqlPrepared = new SqlPrepared();
        String sql =  "DROP TABLE IF EXISTS `"+tableName()+"`;";
        sqlPrepared.setSql(sql);
        return sqlPrepared;
    }

    protected String columnString(@NonNull BeanColumn column){
        String str;
        if(!Strings.isNullOrEmpty(column.getColumnType())){
            str =  column.getColumnType()+"("+column.getColumnLength()+")";
        }else{
            Class clazz = column.getField().getType();
            switch (clazz.getSimpleName().toLowerCase()){
                case "byte":
                    str = "tinyint";
                    break;
                case "short":
                    str = "smallint";
                    break;
                case "int":
                case "integer":
                    str = "int";
                    break;
                case "long":
                    str = "bigint";
                    break;
                case "boolean":
                    str = "tinyint";
                    break;
                default:
                    if(column.getColumnLength()<20000){
                        str = "varchar("+column.getColumnLength()+")";
                    }else{
                        str = "text";
                    }
                    break;
            }
        }
        str = "`"+column.getColumnName()+"` "+str;
        return str;
    }


    private SqlPrepared where(Map<String, Object> condition){
        SqlPrepared sqlPrepared = new SqlPrepared();
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = Maps.newHashMap();
        sql.append(" WHERE 1=1 ");
        if(null!=condition&&!condition.isEmpty()){
            Set<String> keySet = Bean4DbUtil.getFieldNameSet(clazz);
            for(String key:condition.keySet()){
                //添加 条件值 校验
                Preconditions.checkArgument(keySet.contains(key),
                        String.format("[%s] dos not contains field [%s]",clazz,key));
                String column = columnName(key);
                Object value = condition.get(key);
                if(value instanceof Compare){
                    //各种比较
                    Compare compare = (Compare) value;
                    if(null!=compare.getLike()){
                        sql.append(String.format(" AND %s LIKE :%s__like ",column,key));
                        params.put(key+"__like", compare.getLike());
                    }
                    if(null!=compare.getGt()){
                        sql.append(String.format(" AND %s > :%s__gt ",column,key));
                        params.put(key+"__gt", compare.getGt());
                    }
                    if(null!=compare.getLt()){
                        sql.append(String.format(" AND %s < :%s__lt ",column,key));
                        params.put(key+"__lt", compare.getLt());
                    }
                    if(null!=compare.getGte()){
                        sql.append(String.format(" AND %s >= :%s__gte ",column,key));
                        params.put(key+"__gte", compare.getGte());
                    }
                    if(null!=compare.getLte()){
                        sql.append(String.format(" AND %s <= :%s__lte ",column,key));
                        params.put(key+"__lte", compare.getLte());
                    }
                    if(null!=compare.getNe()){
                        sql.append(String.format(" AND %s <> :%s__ne ",column,key));
                        params.put(key+"__ne", compare.getNe());
                    }
                    if(null!=compare.getEmpty()){
                        if(compare.getEmpty()){
                            sql.append(String.format(" AND %s IS NULL  ",column));
                        }else{
                            sql.append(String.format(" AND %s IS NOT NULL  ",column));
                        }
                    }
                    if(null!=compare.getIn()){
                        sql.append(String.format(" AND %s IN (:%s__in) ",column,key));
                        params.put(key+"__in", compare.getIn());
                    }
                    if(null!=compare.getNotIn()){
                        sql.append(String.format(" AND %s NOT IN (:%s__notIn) ",column,key));
                        params.put(key+"__notIn", compare.getNotIn());
                    }
                }else{
                    sql.append(String.format(" AND %s = :%s__eq ",column,key));
                    params.put(key+"__eq", value);
                }
            }
        }
        sqlPrepared.setParams(params);
        sqlPrepared.setSql(sql.toString());
        return sqlPrepared;
    }

    protected String key(){
        return Bean4DbUtil.getKeyFieldName(clazz);
    }

    protected String tableName(){
        return Bean4DbUtil.getTableName(clazz);
    }

    protected String columnName(String fieldName){
        return Bean4DbUtil.getColumnName(clazz,fieldName);
    }

    protected Map<String,Object> toMap(Object bean){
        return Bean4DbUtil.Bean2Map(bean);
    }

    protected boolean isEmtry(Object obj){
        return obj==null;
    }

}
