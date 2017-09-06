package com.jing.cloud.service.dao;

import java.util.List;
import java.util.Map;

import com.jing.cloud.service.bean.BaseBean;
import com.jing.cloud.service.util.db.Page;

public interface DbDao<T> {
	T find(Object id) ;
	List<T> query(Map<String, Object> condition);
	Page<T> query(Map<String, Object> condition, Page<T> page);
	int count(Map<String, Object> condition);
	int insert(T... t);
	int insert(List<T> list);
	int update(T t) ;
	int update(T t, Map<String, Object> condition);
	int delete4List(List<Object> ids);
	int delete(Object... ids);
	int delete4Batch(Map<String,Object> condition);
	int createTable();
	int dropTable();
}
