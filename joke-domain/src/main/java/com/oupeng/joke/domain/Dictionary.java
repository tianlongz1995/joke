package com.oupeng.joke.domain;

import java.util.Date;

/**
 * 数据字典
 * Created by hushuang on 16/9/7.
 */
public class Dictionary {
    /** 编号    */
    private Integer id;
    /** 编码    */
    private String code;
    /** 父级编码    */
    private String parentCode;
    /** 类型    */
    private String type;
    /** 值    */
    private String value;
    /** 描述    */
    private String describe;
    /** 序号    */
    private Integer seq;
    /** 创建时间    */
    private Date createTime;
    /** 更新时间    */
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
