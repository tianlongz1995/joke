package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.domain.IndexResource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 首页资源接口
 * Created by hushuang on 2017/1/3.
 */
public interface ResourceMapper {
    @Select("select max(case `code` when '10011' then `value` else '' end) libJs, max(case `code` when '10012' then `value` else '' end) buildJs, max(case `code` when '10013' then `value` else '' end) buildCss from dictionary where type = '10010' group by type")
    IndexResource getIndexResource();
    @Select("select max(case `code` when '10021' then `value` else '' end) libJs, max(case `code` when '10022' then `value` else '' end) buildJs, max(case `code` when '10023' then `value` else '' end) buildCss from dictionary where type = '10020' group by type")
    IndexResource getIndexResourceBack();
    @Select("select max(case `code` when '10031' then `value` else '' end) libJs, max(case `code` when '10032' then `value` else '' end) buildJs, max(case `code` when '10033' then `value` else '' end) buildCss from dictionary where type = '10030' group by type")
    IndexResource getIndexResourceTest();

    @Update("update dictionary set value = #{value} where code = #{code} and type = #{type}")
    int updateIndex(@Param("value") String value, @Param("code") String code, @Param("type") String type);
}
