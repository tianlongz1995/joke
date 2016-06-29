package com.oupeng.joke.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.oupeng.joke.domain.user.Authority;
import com.oupeng.joke.domain.user.User;
import com.oupeng.joke.domain.user.UserInfo;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
	@Select("SELECT users.username, users.create_date, authorities.authority FROM users INNER JOIN authorities ON users.username = authorities.username WHERE users.username = #{username}")
	UserInfo getUser(@Param("username") String username);
	
	@Select("SELECT users.username, users.create_date, authorities.authority FROM users INNER JOIN authorities ON users.username = authorities.username ORDER BY create_date")
	List<UserInfo> getAll();
	
	@Select("SELECT users.username, users.create_date, t.authority FROM users INNER JOIN (SELECT * FROM authorities WHERE authority = 'ROLE_USER') t ON users.username = t.username ORDER BY create_date")
	List<UserInfo> getUsersByRoleUser();
	
	@Insert("INSERT INTO users (username, password, enabled) VALUES (#{username}, #{password}, 1)")
	//@SelectKey(statement="call next value for TestSequence", keyProperty="nameId", before=true, resultType=int.class)
	void addUser(User user);
	
	@Insert("INSERT INTO authorities (username, authority) VALUES (#{username}, #{authority})")
	void addAuth(Authority authority);
	
	@Delete("DELETE FROM users WHERE username = #{username}")
	int removeUser(@Param("username") String username);
	
	@Delete("DELETE FROM authorities WHERE username = #{username}")
	int removeAuthority(@Param("username") String username);
	
	@Update("UPDATE users SET password = #{password} WHERE username = #{username}")
	int updatePasswordByName(User user);
}