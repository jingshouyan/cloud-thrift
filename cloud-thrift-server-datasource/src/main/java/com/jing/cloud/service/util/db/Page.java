package com.jing.cloud.service.util.db;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Page<T> {

	public static final String ASC = "asc";
	public static final String DESC = "desc";

	private long page = 1;
	private long pageSize = 10;
	private long totalPage;
	private long totalCount;
	private String orderBy;
	private String sort = ASC;
	private List<T> list;



	public void totalCount(long totalCount) {
		long tp = totalCount / pageSize;
		long y = totalCount % pageSize;
		if (0 != y) {
			tp++;
		}
		setTotalPage(tp);
		this.totalCount = totalCount;
	}

}
