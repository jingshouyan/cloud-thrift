package com.jing.cloud.service.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.common.base.Preconditions;
import com.jing.cloud.service.bean.BaseBean;
import com.jing.cloud.service.util.db.DbInfoUtil;
import com.jing.cloud.service.util.keygen.DefaultKeyGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jing.cloud.service.dao.DbDao;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.Page;
import com.jing.cloud.service.util.bean.BeanUtil;
import com.jing.cloud.service.util.bean.StrFormat;

@Repository
public abstract class DbDaoImpl<T extends BaseBean>  implements DbDao<T> {

	public static final String TABLE_PREFIX = "";

	public static final Logger logger = LoggerFactory.getLogger(DbDaoImpl.class);

	private Class<T> clazz;

	public DbDaoImpl(){
		init();
	}

//	@Override
	public void setClass(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@SuppressWarnings("unchecked")
	public void init(){
		Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            clazz = (Class<T>)p[0];
        }
	}

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 
	* @Title: find 
	* @Description: 根据主键查询单条
	* @param @param id
	* @param @return    设定文件 
	* @return T    返回类型 
	* @throws
	 */
	@Override
	public T find(long id) {
		String sql = "select * from " + tableName() + " where " + dbKey() + "=:id";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);		
		RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);
		logger.info(sql+paramMap.toString());
		List<T> ts = template.query(sql, paramMap, rowMapper);
		if (ts.isEmpty()) {
			return null;
		}		
		return ts.get(0);
	}
	
	
	/**
	 * 
	 * query:分页查询记录. <br/>
	 *
	 * @author bxy-jing
	 * @param condition 查询条件
	 * @param page 分页情况
	 * @return
	 * @since JDK 1.6
	 */
	@Override
	public Page<T> query(Map<String,Object> condition,Page<T> page){
		long count = count(condition);
		page.totalCount(count);
		//查询该页记录，拼装查询sql
		StringBuilder sb = new StringBuilder();
		sb.append("select * from "+tableName()+" where 1=1 ");
		WhereResult result = where(condition);
		sb.append(result.getSql());

		//如果有orderBy选项，添加order by
		if(page.getOrderBy()!=null){
			sb.append(" order by "+field2DbColumn(page.getOrderBy())+" "+page.getSort()+" ");
		}
		//计算
		long limit = (page.getPage()-1)*page.getPageSize();
		sb.append(" limit "+limit+", "+page.getPageSize()+" ");
		String sql = sb.toString();
		logger.info("sql:[{}],param:[{}]",sql,result.getParam());
		RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);
		List<T> ts = template.query(sql, result.getParam(), rowMapper);
		page.setList(ts);
		return page;
	}
	
	/**
	 * 
	* @Title: query 
	* @Description: 条件查询多条记录
	* @param @param condition
	* @param @return    设定文件 
	* @return List<T>    返回类型 
	* @throws
	 */
	@Override
	public List<T> query(Map<String, Object> condition){
		StringBuilder sb = new StringBuilder();
		sb.append("select * from "+tableName()+" where 1=1 ");
		WhereResult result = where(condition);
		sb.append(result.getSql());
		String sql = sb.toString();
		logger.info("sql:[{}],param:[{}]",sql,result.getParam());
		RowMapper<T> rowMapper = new BeanPropertyRowMapper<>(clazz);
		List<T> ts = template.query(sql, result.getParam(), rowMapper);
		return ts;
	}

	@Override
	public int batchUpdate(T t,Map<String, Object> condition){
		int count = 0;
		StringBuilder sb = new StringBuilder();
		Map<String,Object> valueMap = BeanUtil.Obj2Map(t);
		Map<String,Object> setMap = new HashMap<>();
		sb.append("update "+tableName()+" set ");
		for(String key:valueMap.keySet()){
			Object value = valueMap.get(key);
			// 空 不更新
			if(isEmtry(value)){
				continue;
			}
			//主键不更新
			if(key.equals(key())){
				continue;
			}
			String column = field2DbColumn(key);
			sb.append(" "+column+" = :"+key+"__set ,");
			setMap.put(key+"__set",value);
		}
		//只有当有字段需要更新
		if(!setMap.isEmpty()){
			//移除set 最后一个逗号 ,
			sb.deleteCharAt(sb.length()-1);

			sb.append(" where 1=1 ");
			WhereResult whereResult = where(condition);
			sb.append(whereResult.getSql());
			setMap.putAll(whereResult.getParam());
			String sql = sb.toString();
			logger.info(sql + String.valueOf(setMap));
			count = template.update(sql, setMap);
		}
		return count;
	}
	
	/**
	 * 
	 * count:查询满足挑你的记录行数
	 *
	 * @author bxy-jing
	 * @param condition
	 * @return
	 * @since JDK 1.6
	 */
	@Override
	public long count(Map<String, Object> condition){
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from "+tableName()+" where 1=1 ");
		WhereResult result = where(condition);
		sb.append(result.getSql());
		String sql = sb.toString();
		logger.info("sql:[{}],param:[{}]",sql,result.getParam());
		long count = template.queryForObject(sql, result.getParam(), Long.class);
		return count;
	}
	
	/**
	 * 
	 * where:根据查询条件拼装sql. <br/>
	 * 且将map重构
	 * @author bxy-jing
	 * @param condition 查询条件
	 * @return
	 * @since JDK 1.6
	 */
	private WhereResult where(Map<String, Object> condition){
		WhereResult result = new WhereResult();
		if(null!=condition&&!condition.isEmpty()){
			StringBuilder sb = new StringBuilder();
			Map<String,Object> mapB = new HashMap<>();
			for(String key:condition.keySet()){
				String column = field2DbColumn(key);
				Object value = condition.get(key);
				if(value instanceof Compare){
					//各种比较
					Compare compare = (Compare) value;
					if(null!=compare.getLike()){
						sb.append(" and " + column + " like :" + key+"__like ");
						mapB.put(key+"__like", compare.getLike());
					}
 					if(null!=compare.getGt()){
						sb.append(" and " + column + ">:" + key+"__gt ");
						mapB.put(key+"__gt", compare.getGt());
					}
					if(null!=compare.getLt()){
						sb.append(" and " + column + "<:" + key+"__lt ");
						mapB.put(key+"__lt", compare.getLt());
					}
					if(null!=compare.getGte()){
						sb.append(" and " + column + ">=:" + key+"__gte ");
						mapB.put(key+"__gte", compare.getGte());
					}
					if(null!=compare.getLte()){
						sb.append(" and " + column + "<=:" + key+"__lte ");
						mapB.put(key+"__lte", compare.getLte());
					}
					if(null!=compare.getNe()){
						sb.append(" and " + column + "<>:" + key+"__ne ");
						mapB.put(key+"__ne", compare.getNe());
					}
					if(null!=compare.getEmpty()){
						if(compare.getEmpty()){
							sb.append(" and " + column +" is null ");
						}else{
							sb.append(" and " + column +" is not null ");
						}
					}
					if(null!=compare.getIn()){
						sb.append(" and " + column + " in (:" + key+"__in) ");
						mapB.put(key+"__in", compare.getIn());
					}
					if(null!=compare.getNotIn()){
						sb.append(" and " + column + " not in (:" + key+"__notIn)" );
						mapB.put(key+"__notIn", compare.getNotIn());
					}
				}else{
					sb.append(" and " + column + "=:" + key+"__eq ");
					mapB.put(key+"__eq", value);
				}
			}
			result.setSql(sb.toString());
			result.setParam(mapB);
		}
		return result;
	}

	
	/**
	 * 
	* @Title: insert 
	* @Description: 新增记录
	* @param @param t    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public int insert(T t) {
		genKey(t);
		StringBuilder sb = new StringBuilder();
		StringBuilder keys = new StringBuilder();
		StringBuilder values = new StringBuilder();
		Map<String, Object> valueMap = BeanUtil.Obj2Map(t);

		for (String key : valueMap.keySet()) {
			// 空数据不插入
			if (isEmtry(valueMap.get(key))) {
				continue;
			}
			keys.append(field2DbColumn(key) + ",");
			values.append(":" + key + ",");
		}
		if (keys.length() > 0) {
			keys.deleteCharAt(keys.length() - 1);
			values.deleteCharAt(values.length() - 1);
		}
		sb.append("insert into " + tableName());
		sb.append("(" + keys.toString() + ") ");
		sb.append(" values(" + values.toString() + ")");
		String sql = sb.toString();
		logger.info(sql + valueMap.toString());
		return template.update(sql, valueMap);
	}

	@Override
	public int batchInsert(List<T> list){
		//如果list为空，直接返回
		if(null==list||list.isEmpty()){
			return 0;
		}
		for(T t:list){
			//如果不设置主键，则使用 keygen 生成主键
			genKey(t);
		}

		T t = list.get(0);
		Map<String,Object> map = BeanUtil.Obj2Map(t);
		//如果model没有属性，直接返回
		if(map.isEmpty()){
			return 0;
		}
		
		StringBuilder sb = new StringBuilder();
		StringBuilder keys = new StringBuilder();
		StringBuilder values = new StringBuilder();
		values.append(" values");
		int i=0;
		Map<Integer,String> keyMap = new HashMap<>();
		Map<String,Object> valueMap = new HashMap<>();
		for(String key:map.keySet()){
			// 空值不插入
			if (isEmtry(map.get(key))) {
				continue;
			}
			//拼装keys字符串
			keys.append(field2DbColumn(key) + ",");
			//将所有的key标上序号放在map中，待用
			keyMap.put(i, key);
			i++;
		}
		keys.deleteCharAt(keys.length()-1);
		int listSize = list.size();
		for(int j=0;j<listSize;j++){
			t = list.get(j);
			map = BeanUtil.Obj2Map(t);
			StringBuilder valueSb = new StringBuilder();
			for(int k = 0;k<i;k++){
				String key = keyMap.get(k)+"_"+j+"_"+k;
				valueSb.append(":"+key+",");
				valueMap.put(key, map.get(keyMap.get(k)));
			}
			valueSb.deleteCharAt(valueSb.length()-1);
			values.append("("+valueSb.toString()+"),");
		}
		values.deleteCharAt(values.length()-1);
		sb.append("insert into " + tableName());
		sb.append(" (" + keys.toString() + ") ");
		sb.append(values.toString());
		
		String sql = sb.toString();
		logger.info(sql + valueMap.toString());
		return template.update(sql, valueMap);
	}



	/**
	 * 
	* @Title: update 
	* @Description: 更新记录
	* @param @param t    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public int update(T t) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sets = new StringBuilder();
		StringBuilder where = new StringBuilder();
		where.append(" where 1=1 ");
		Map<String, Object> valueMap = BeanUtil.Obj2Map(t);
		Map<String, Object> param = new HashMap<>();
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
		Preconditions.checkArgument(setKey,"key value is not set!");
		Preconditions.checkArgument(sets.length() > 0,"nothing to update!");
			sets.deleteCharAt(sets.length() - 1);
		sb.append("update " + tableName() + " set ");
		sb.append(sets.toString());
		sb.append(where.toString());
		String sql = sb.toString();
		logger.info("sql:[{}],param:[{}]",sql,param);
		int fetch = template.update(sql, param);
		return fetch;
	}

	/**
	 * 
	* @Title: delete 
	* @Description: 通过主键删除记录
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@Override
	public int delete(long id) {
		String sql = "delete from " + tableName() + " where " + dbKey() + "=:id";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", id);
		logger.info(sql+paramMap.toString());
		return template.update(sql, paramMap);
	}
	@Override
	public int delete(List<Long> ids){
		String sql = "delete from " + tableName() + " where " + dbKey() + " in (:id) ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", ids);
		logger.info(sql+paramMap.toString());
		return template.update(sql, paramMap);
	}
	@Override
	public int delete(long[] ids){
		List<Long> l= new ArrayList<>();
		for(Long id:ids){
			l.add(id);
		}
		return delete(l);
	}

	/**
	 *
	 * @return
	 */
	public String tableName() {
		String className = clazz.getSimpleName();
		String tableName = StrFormat.camel2Underline(className);
		return TABLE_PREFIX + tableName;
	}
	

	protected String field2DbColumn(String field){
		return field;
	}

	/**
	 * 
	 * key:JAVABEAN中主键名. <br/>
	 *
	 * @author bxy-jing
	 * @return
	 * @since JDK 1.6
	 */
	private String key() {
		return DbInfoUtil.getKeyName(clazz);
	}

	/**
	 *
	 * @param t
	 */
	private void genKey(T t){
		//设置了@GenKey注解 并且 没有赋值
		if(null!=DbInfoUtil.getKeyName(t.getClass())
				&&null==DbInfoUtil.getKey(t)){
			//使用 DefaultKeyGenerator 给这个属性赋值
			long genKey = DefaultKeyGenerator.getInstance().generateKey().longValue();
			DbInfoUtil.setKey(t,genKey);
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
	 * version:记录版本号对应的JAVABEAN属性名. <br/>
	 *
	 * @author bxy-jing
	 * @return
	 * @since JDK 1.6
	 */
	private String version() {
		return DbInfoUtil.getVersionName(clazz);
	}

	
	/**
	 * 
	 * isEmtry:判断是否为空 这里只有null表示空 <br/>
	 *
	 * @author bxy-jing
	 * @param o
	 * @return
	 * @since JDK 1.6
	 */
	public static boolean isEmtry(Object o) {
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
		Map<String,Object> param = new HashMap<>();
	}

}
