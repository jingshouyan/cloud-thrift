package com.jing.cloud.service.util.db;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 
* @ClassName: Compare
* @Description: 数据查询各种比较
* @author 靖守彦 jingshouyan@126.com
* @date 2015年12月11日 下午6:01:59
*
 */
@Data
@ToString
public class Compare {	

	private String like;

	private Object gt;//大于
	private Object lt;//小于 
	private Object gte;//大于等于
	private Object lte;//小于等于
	private Object ne;//不等于

	private List<?> in;//在范围

	private List<?> notIn;//不在范围

	private Boolean empty;// 是否为空

	
}
