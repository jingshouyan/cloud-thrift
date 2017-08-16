package com.jing.cloud.service.util.db;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
	private long page = 1;
	private long pageSize = 10;
	private long totalPage;
	private long totalCount;
	private List<T> list;

	private List<OrderBy> orderBies = Lists.newArrayList();

	public void totalCount(long totalCount) {
		long tp = totalCount / pageSize;
		long y = totalCount % pageSize;
		if (0 != y) {
			tp++;
		}
		setTotalPage(tp);
		this.totalCount = totalCount;
	}

	public void addOrderBy(OrderBy orderBy){
		orderBies.add(orderBy);
	}
}
