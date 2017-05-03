package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.SensitiveSqlProvider;
import com.oupeng.joke.domain.Sensitive;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * Created by java_zong on 2017/4/26.
 */
public interface SensitiveMapper {

    @Select(" select word from sensitive_words where status = 1 ")
    Set<String> getSensitiveWord();

    @SelectProvider(method = "getListForCount", type = SensitiveSqlProvider.class)
    int getListForCount(String keyWord);

    @SelectProvider(method = "getList", type = SensitiveSqlProvider.class)
    List<Sensitive> getList(@Param("keyWord") String keyWord, @Param("offset") int offset, @Param("pageSize") Integer pageSize);

    /**
     * 添加敏感词
     *
     * @param word
     */
    @Insert("insert into sensitive_words (`word`) values (#{word})")
    void insertWord(String word);

    /**
     * 删除敏感词
     *
     * @param id
     */
    @Delete("delete from sensitive_words where id = #{id}")
    void deleteWord(Integer id);

    @Select("select word from sensitive_words where id = #{id}")
    String getWordById(Integer id);
}
