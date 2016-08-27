package com.oupeng.joke.domain.statistics;

public class DropDetail {
	/**	日期	*/
	private Integer time;
	/**	渠道名称	*/
	private String dName;
	/**	频道名称	*/
	private String cName;
	/**	总PV	*/
	private Integer totalPv;
	/**	总UV	*/
	private Integer totalUv;

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}

	public Integer getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(Integer totalPv) {
		this.totalPv = totalPv;
	}

	public Integer getTotalUv() {
		return totalUv;
	}

	public void setTotalUv(Integer totalUv) {
		this.totalUv = totalUv;
	}
}
