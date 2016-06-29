package com.oupeng.joke.domain.user;

import java.util.Date;

public class UserInfo {
	private String username;
    private String authority;
    private Date create_date;
    
    public UserInfo(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}	
}
