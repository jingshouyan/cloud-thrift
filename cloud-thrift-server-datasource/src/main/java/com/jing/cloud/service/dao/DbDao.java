package com.jing.cloud.service.dao;

import java.util.List;
import java.util.Map;

import com.jing.cloud.service.bean.BaseBean;
import com.jing.cloud.service.util.db.Page;

public interface DbDao<T extends BaseBean> {
	T find(long id) ;
	List<T> query(Map<String, Object> condition);
	Page<T> query(Map<String, Object> condition, Page<T> page);
	long count(Map<String, Object> condition);
	int insert(T t);
	int batchInsert(List<T> list);
	int update(T t) ;
	int batchUpdate(T t,Map<String, Object> condition);
	int delete(long id);
	int delete(List<Long> ids);
	int delete(long[] ids);
	int batchDelete(Map<String,Object> condition);
}
