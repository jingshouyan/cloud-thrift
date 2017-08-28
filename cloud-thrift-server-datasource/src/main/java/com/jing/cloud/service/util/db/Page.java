package com.jing.cloud.service.util.db;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
	private int page = 1;
	private int pageSize = 10;
	private int totalPage;
	private int totalCount;
	private List<T> list;

	private List<OrderBy> orderBies = Lists.newArrayList();

	public void totalCount(int totalCount) {
		int tp = totalCount / pageSize;
		int y = totalCount % pageSize;
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
