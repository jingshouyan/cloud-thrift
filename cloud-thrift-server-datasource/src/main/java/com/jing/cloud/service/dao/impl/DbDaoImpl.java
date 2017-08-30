package com.jing.cloud.service.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jing.cloud.service.dao.DbDao;
import com.jing.cloud.service.util.db.Bean4DbUtil;
import com.jing.cloud.service.util.db.BeanRowMapper;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.Page;
import com.jing.cloud.service.util.db.SqlPrepared;
import com.jing.cloud.service.util.db.sql.generator.SqlGenerator;
import com.jing.cloud.service.util.db.sql.generator.SqlGeneratorFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
	public int update(T t, Map<String, Object> condition){
        encrypt(t);
		SqlPrepared sqlPrepared = sqlGenerator.update(t,condition);
		return template.update(sqlPrepared.getSql(), sqlPrepared.getParams());
	}




	@Override
	public int count(Map<String, Object> condition){
		SqlPrepared sqlPrepared = sqlGenerator.count(condition);
        return template.queryForObject(sqlPrepared.getSql(), sqlPrepared.getParams(), Integer.class);
	}

	

	@Override
	public int insert(T... ts) {
		List<T> list = Lists.newArrayList(ts);
		return insert(list);
	}

	@Override
	public int insert(@NonNull List<T> list){
		Preconditions.checkArgument(!list.isEmpty(),"list is empty!");
		for(T t:list){
			//如果不设置主键，则使用 keygen 生成主键
			genKey(t);
            encrypt(t);
		}
		SqlPrepared sqlPrepared = sqlGenerator.insert(list);
		return template.update(sqlPrepared.getSql(), sqlPrepared.getParams());
	}

	@Override
	public int update(T t) {
		String key = key();
		Object value = Bean4DbUtil.getFieldValue(t,key);
		Preconditions.checkArgument(null!=value,"The @Key field must not null");
		Map<String,Object> condition = Maps.newHashMap();
		condition.put(key,value);
		return update(t,condition);
	}

	@Override
	public int delete(List<Object> ids){
		Map<String, Object> param = Maps.newHashMap();
        Compare compareId = new Compare();
        compareId.setIn(ids);
		param.put(key(), compareId);
        return delete4Batch(param);
	}

	@Override
	public int delete(Object... ids){
		List<Object> l= Lists.newArrayList(ids);
		return delete(l);
	}

	@Override
	public int delete4Batch(Map<String,Object> condition){
		SqlPrepared sqlPrepared = sqlGenerator.delete(condition);
		return template.update(sqlPrepared.getSql(), sqlPrepared.getParams());
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






	private String key() {
		return Bean4DbUtil.getKeyFieldName(clazz);
	}

	/**
	 * 生成主键
	 * @param t 对象
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
}
