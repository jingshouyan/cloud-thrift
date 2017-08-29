package com.jing.cloud.service.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jing.cloud.service.dao.DbDao;
import com.jing.cloud.service.util.db.Bean4DbUtil;
import com.jing.cloud.service.util.db.BeanRowMapper;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.OrderBy;
import com.jing.cloud.service.util.db.Page;
import com.jing.cloud.service.util.db.SqlPrepared;
import com.jing.cloud.service.util.db.sql.generator.SqlGenerator;
import com.jing.cloud.service.util.db.sql.generator.SqlGeneratorFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class DbDaoImpl<T>  implements DbDao<T> {


	private Class<T> clazz;
	private RowMapper<T> rowMapper;
	private SqlGenerator<T> sqlGenerator;

	public DbDaoImpl(){
		init();
	}

	@SuppressWarnings("unchecked")
	private void init(){
		Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            clazz = (Class<T>)p[0];
			rowMapper = new BeanRowMapper<>(clazz);
			sqlGenerator = SqlGeneratorFactory.getSqlGenerator(clazz);
        }
	}

	@Autowired
	private NamedParameterJdbcTemplate template;


	@Override
	public T find(Object id) {
		Map<String, Object> condition = Maps.newHashMap();
		condition.put(key(), id);
		List<T> ts = query(condition);
		if (ts.isEmpty()) {
			return null;
		}
		return ts.get(0);
	}
	
	

	@Override
	public Page<T> query(Map<String,Object> condition,Page<T> page){
		int count = count(condition);
		page.totalCount(count);
		SqlPrepared sqlPrepared = sqlGenerator.query(condition,page);
		List<T> ts = template.query(sqlPrepared.getSql(), sqlPrepared.getParams(), rowMapper);
		decrypt(ts);
		page.setList(ts);
		return page;
	}
	

	@Override
	public List<T> query(Map<String, Object> condition){
		SqlPrepared sqlPrepared = sqlGenerator.query(condition);
		List<T> ts = template.query(sqlPrepared.getSql(), sqlPrepared.getParams(), rowMapper);
        //解密
		decrypt(ts);
		return ts;
	}

	@Override
	public int batchUpdate(T t,Map<String, Object> condition){
        encrypt(t);
		SqlPrepared sqlPrepared = sqlGenerator.batchUpdate(t,condition);
		return template.update(sqlPrepared.getSql(), sqlPrepared.getParams());
	}

	@Override
	public int batchDelete(Map<String,Object> condition){
		SqlPrepared sqlPrepared = sqlGenerator.delete(condition);
		return template.update(sqlPrepared.getSql(), sqlPrepared.getParams());
	}


	@Override
	public int count(Map<String, Object> condition){
		SqlPrepared sqlPrepared = sqlGenerator.delete(condition);
        return template.queryForObject(sqlPrepared.getSql(), sqlPrepared.getParams(), Integer.class);
	}

	

	@Override
	public int insert(T t) {
		List<T> ts  = new ArrayList<>(1);
		ts.add(t);
		return batchInsert(ts);
	}

	@Override
	public int batchInsert(@NonNull List<T> list){
		Preconditions.checkArgument(!list.isEmpty(),"list mustn't be empty");
		for(T t:list){
			//如果不设置主键，则使用 keygen 生成主键
			genKey(t);
            encrypt(t);
		}
		SqlPrepared sqlPrepared = sqlGenerator.batchInsert(list);
		return template.update(sqlPrepared.getSql(), sqlPrepared.getParams());
	}

	@Override
	public int update(T t) {

		StringBuilder sb = new StringBuilder();
		StringBuilder sets = new StringBuilder();
		StringBuilder where = new StringBuilder();
		where.append(" where 1=1 ");
		Map<String, Object> valueMap = Bean4DbUtil.Bean2Map(t);
		Map<String, Object> param = Maps.newHashMap();
		boolean setKey = false; //是否设置了key
		for (String key : valueMap.keySet()) {
			//排除 空 值
			if(isEmtry(valueMap.get(key))){
				continue;
			}
			String underScoreKey = field2DbColumn(key);
			if (key.equals(key())) {
				setKey = true;
				where.append(" and " + underScoreKey + "=:" + key);
			} else if (key.equals(version())) {
				sets.append(" " + underScoreKey + "=" + underScoreKey + "+1,");
				where.append(" and " + underScoreKey + "=:" + key);
			} else {
				sets.append(" " + underScoreKey + "=:" + key + ",");
			}
			param.put(key,valueMap.get(key));
		}
		Preconditions.checkArgument(setKey,"keyColumn value is not set!");
		Preconditions.checkArgument(sets.length() > 0,"nothing to update!");
			sets.deleteCharAt(sets.length() - 1);
		sb.append("update " + tableName() + " set ");
		sb.append(sets.toString());
		sb.append(where.toString());
		String sql = sb.toString();
		int count = template.update(sql, param);
		return count;
	}

	@Override
	public int delete(List<Object> ids){
		Map<String, Object> param = Maps.newHashMap();
        Compare compareId = new Compare();
        compareId.setIn(ids);
		param.put(key(), compareId);
        return batchDelete(param);
	}
	@Override
	public int delete(Object... ids){
		List<Object> l= Lists.newArrayList();
		for(Object id:ids){
			l.add(id);
		}
		return delete(l);
	}

	@Override
	public int createTable(){
		SqlPrepared sqlPrepared = sqlGenerator.createTableSql();
		return template.update(sqlPrepared.getSql(),sqlPrepared.getParams());
	}

	@Override
	public int dropTable(){
		SqlPrepared sqlPrepared = sqlGenerator.dropTableSql();
		return template.update(sqlPrepared.getSql(),sqlPrepared.getParams());
	}


	/**
	 *
	 * @return
	 */
	private String tableName() {
		return Bean4DbUtil.getTableName(clazz);
	}
	

	private String field2DbColumn(String field){
		String columnName = Bean4DbUtil.getColumnName(clazz,field);

		return columnName;
	}

	/**
	 * 
	 * keyColumn:JAVABEAN中主键名. <br/>
	 *
	 * @author bxy-jing
	 * @return
	 * @since JDK 1.6
	 */
	private String key() {
		return Bean4DbUtil.getKeyFieldName(clazz);
	}

	/**
	 *
	 * @param t
	 */
	private void genKey(T t){
		Bean4DbUtil.genKey(t);
	}

    /**
     * 加密处理
     * @param t 对象
     */
	private void encrypt(T t){
	    Bean4DbUtil.encryptOrDecryptBean(t,true);
    }

    /**
     * 解密处理
     * @param t 对象
     */
    private void decrypt(T t){
        Bean4DbUtil.encryptOrDecryptBean(t,false);
    }


    private void decrypt(List<T> ts){
		if(null!=ts&&!ts.isEmpty()){
			for (T t:ts) {
				decrypt(t);
			}
		}
	}
	/**
	 * 
	 * dbKey:. <br/>
	 * 数据库主键名.<br/>
	 *
	 * @author bxy-jing
	 * @return
	 * @since JDK 1.6
	 */
	protected String dbKey(){
		return field2DbColumn(key());
	}

	/**
	 * 
	 * versionColumn:记录版本号对应的JAVABEAN属性名. <br/>
	 *
	 * @author bxy-jing
	 * @return
	 * @since JDK 1.6
	 */
	private String version() {
		return Bean4DbUtil.getVersionFieldName(clazz);
	}


	/**
	 * 判断是否为空 目前 null 为空
	 * @param o 对象
	 * @return 结果
	 */
	private static boolean isEmtry(Object o) {
		if (null == o) {
			return true;
		}

		return false;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	private class WhereResult{
		String sql = "";
		Map<String,Object> param = Maps.newHashMap();
	}

}
