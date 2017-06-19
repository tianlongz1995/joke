package com.oupeng.joke.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by pengzheng on 17-6-14.
 */
public class BlackMan {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nick;
    @JsonInclude(JsonInclude.Include.NON_NULL)

    private Date create_time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String create_by;

    public Date getCreate_time() {
        return create_time;

    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
