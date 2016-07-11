package com.oupeng.joke.domain;

/**
 * 反馈类型统计
 * Created by hushuang on 16/7/9.
 */
public class LineDataSource {
    /** 反馈日期    */
    private String createTime;
    /** 刷新慢类型数量    */
    private Integer refresh;
    /** 不好笑类型数量    */
    private Integer noFunny;
    /** 闪退类型数量    */
    private Integer quit;
    /** 其他类型数量    */
    private Integer other;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getRefresh() {
        return refresh;
    }

    public void setRefresh(Integer refresh) {
        this.refresh = refresh;
    }

    public Integer getNoFunny() {
        return noFunny;
    }

    public void setNoFunny(Integer noFunny) {
        this.noFunny = noFunny;
    }

    public Integer getQuit() {
        return quit;
    }

    public void setQuit(Integer quit) {
        this.quit = quit;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LineDataSource{");
        sb.append("createTime='").append(createTime).append('\'');
        sb.append(", refresh=").append(refresh);
        sb.append(", noFunny=").append(noFunny);
        sb.append(", quit=").append(quit);
        sb.append(", other=").append(other);
        sb.append('}');
        return sb.toString();
    }
}
