package com.oupeng.joke.dao.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * Created by java_zong on 2017/4/26.
 */
public interface SensitiveMapper {

    @Select(" select word from sensitive_words where status = 1 ")
    Set<String> getSensitiveWord();
}
