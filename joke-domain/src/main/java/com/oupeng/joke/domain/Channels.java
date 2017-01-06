package com.oupeng.joke.domain;

public class Channels {
	/**	主键	*/
	private Integer id;
	/**	频道名称	*/
	private String name;
	/**	排序位置	*/
	private Integer sort;

	public Channels() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}