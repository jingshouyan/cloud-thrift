package com.jing.cloud.service.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jing.cloud.service.bean.BaseBean;
import com.jing.cloud.service.dao.DbDao;
import com.jing.cloud.service.util.db.Bean4DbUtil;
import com.jing.cloud.service.util.db.BeanRowMapper;
import com.jing.cloud.service.util.db.Compare;
import com.jing.cloud.service.util.db.OrderBy;
import com.jing.cloud.service.util.db.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class DbDaoImpl<T extends BaseBean>  implements DbDao<T> {


	private Class<T> clazz;
	private RowMapper<T> rowMapper;

	public DbDaoImpl(){
		init();
	}

	@SuppressWarnings("unchecked")
	public void init(){
		Type t = getClass().getGenericSuperclass();
        if(t instanceof ParameterizedType){
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            clazz = (Class<T>)p[0];
			rowMapper = new BeanRowMapper<>(clazz);
        }
	}

	@Autowired
	private NamedParameterJdbcTemplate template;


	@Override
	public T find(long id) {
		String sql = "select * from " + tableName() + " where " + dbKey() + "=:id";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", id);
		List<T> ts = template.query(sql, param, rowMapper);
		if (ts.isEmpty()) {
			return null;
		}
		return ts.get(0);
	}
	
	

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
		if(null!=page.getOrderBies()&&!page.getOrderBies().isEmpty()){
			sb.append(" order by ");
			for(OrderBy orderBy:page.getOrderBies()){
				sb.append(field2DbColumn(orderBy.getKey()));
				sb.append(" ");
				sb.append(orderBy.isAsc()?" asc ":"desc ");
				sb.append(",");
			}
			//删除最后一个 ,
			sb.deleteCharAt(sb.length() -1);
		}
		//计算
		long limit = (page.getPage()-1)*page.getPageSize();
		sb.append(" limit "+limit+", "+page.getPageSize()+" ");
		String sql = sb.toString();

		List<T> ts = template.query(sql, result.getParam(), rowMapper);

		page.setList(ts);
		return page;
	}
	

	@Override
	public List<T> query(Map<String, Object> condition){
		StringBuilder sb = new StringBuilder();
		sb.append("select * from "+tableName()+" where 1=1 ");
		WhereResult result = where(condition);
		sb.append(result.getSql());
		String sql = sb.toString();
		List<T> ts = template.query(sql, result.getParam(), rowMapper);
		return ts;
	}

	@Override
	public int batchUpdate(T t,Map<String, Object> condition){
		int count ;
		StringBuilder sb = new StringBuilder();
		Map<String,Object> valueMap = Bean4DbUtil.Bean2Map(t);
		Map<String,Object> param = Maps.newHashMap();
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
			param.put(key+"__set",value);
		}
		//只有当有字段需要更新
		Preconditions.checkArgument(!param.isEmpty(),"nothing to update");
		//移除set 最后一个逗号 ,
		sb.deleteCharAt(sb.length()-1);

		sb.append(" where 1=1 ");
		WhereResult whereResult = where(condition);
		sb.append(whereResult.getSql());
		param.putAll(whereResult.getParam());
		String sql = sb.toString();
		count = template.update(sql, param);
		return count;
	}

	@Override
	public int batchDelete(Map<String,Object> condition){
		int count ;
		StringBuilder sb = new StringBuilder();
		sb.append("delete from "+tableName());
		sb.append(" where 1=1 ");
		WhereResult result = where(condition);
		sb.append(result.getSql());
		String sql = sb.toString();
		count = template.update(sql, result.getParam());
		return count;
	}


	@Override
	public long count(Map<String, Object> condition){
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from "+tableName()+" where 1=1 ");
		WhereResult result = where(condition);
		sb.append(result.getSql());
		String sql = sb.toString();
		long count = template.queryForObject(sql, result.getParam(), Long.class);
		return count;
	}
	
	/**
	 * 
	 * where:根据查询条件拼装sql. <br/>
	 * 且将map重构
	 * @param condition 查询条件
	 * @return sql 及 条件
	 * @since JDK 1.6
	 */
	private WhereResult where(Map<String, Object> condition){
		WhereResult result = new WhereResult();
		if(null!=condition&&!condition.isEmpty()){
			StringBuilder sb = new StringBuilder();
			Map<String,Object> mapB = Maps.newHashMap();
			Set<String> keySet = Bean4DbUtil.getFieldNameSet(clazz);
			for(String key:condition.keySet()){
				//添加 条件值 校验
				Preconditions.checkArgument(keySet.contains(key),
						String.format("[%s] dos not contains field [%s]",clazz,key));
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

	

	@Override
	public int insert(T t) {
		genKey(t);
		StringBuilder sb = new StringBuilder();
		StringBuilder keys = new StringBuilder();
		StringBuilder values = new StringBuilder();
		Map<String, Object> param = Bean4DbUtil.Bean2Map(t);

		for (String key : param.keySet()) {
			// 空数据不插入
			if (isEmtry(param.get(key))) {
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
		int count = template.update(sql, param);
		return count;
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
		Map<String,Object> map = Bean4DbUtil.Bean2Map(t);
		//如果model没有属性，直接返回
		if(map.isEmpty()){
			return 0;
		}
		
		StringBuilder sb = new StringBuilder();
		StringBuilder keys = new StringBuilder();
		StringBuilder values = new StringBuilder();
		values.append(" values");
		int i=0;
		Map<Integer,String> keyMap = Maps.newHashMap();
		Map<String,Object> param = Maps.newHashMap();
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
			map = Bean4DbUtil.Bean2Map(t);
			StringBuilder valueSb = new StringBuilder();
			for(int k = 0;k<i;k++){
				String key = keyMap.get(k)+"_"+j+"_"+k;
				valueSb.append(":"+key+",");
				param.put(key, map.get(keyMap.get(k)));
			}
			valueSb.deleteCharAt(valueSb.length()-1);
			values.append("("+valueSb.toString()+"),");
		}
		values.deleteCharAt(values.length()-1);
		sb.append("insert into " + tableName());
		sb.append(" (" + keys.toString() + ") ");
		sb.append(values.toString());
		
		String sql = sb.toString();
		int count = template.update(sql, param);
		return count;
	}

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
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", id);
		int count = template.update(sql, param);
		return count;
	}
	@Override
	public int delete(List<Long> ids){
		String sql = "delete from " + tableName() + " where " + dbKey() + " in (:id) ";
		Map<String, Object> param = Maps.newHashMap();
		param.put("id", ids);
		int count = template.update(sql, param);
		return count;
	}
	@Override
	public int delete(long[] ids){
		List<Long> l= Lists.newArrayList();
		for(Long id:ids){
			l.add(id);
		}
		return delete(l);
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
