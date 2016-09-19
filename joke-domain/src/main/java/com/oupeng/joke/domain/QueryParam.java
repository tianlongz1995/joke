package com.oupeng.joke.domain;

/**
 * 页面查询参数
 * Created by hushuang on 16/9/13.
 */
public class QueryParam {
    /** 开始时间    */
    private String startTime;
    /** 结束时间    */
    private String endTime;
    /** 名称    */
    private String name;
    /** 类型    */
    private Integer type;
    /** 数据源类型    */
    private Integer sourceType;
    /** 状态    */
    private Integer status;
    /** 页码    */
    private Integer pageNumber;
    /** 每页记录数    */
    private Integer pageSize;
    /**	分页起始位置	*/
    private Integer offset;
    /**	排序字段	*/
    private String orderField;
    /**	排序规则	*/
    private String order;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPageNumber() {
        return pageNumber == null ? 1 : pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
