package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.BlackManSqlProvider;
import com.oupeng.joke.domain.user.BlackMan;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * Created by Jane on 17-6-14.
 */
public interface BlackManMapper {

    @SelectProvider(method = "getABlackMantoBeLaHei", type = BlackManSqlProvider.class)
    int getABlackMan(String id);

    @InsertProvider(method="insertABlackMantoBeLaHei",type = BlackManSqlProvider.class)
    int insertABlackMan(BlackMan sb);

    @DeleteProvider(method="deleteBlackMantoBeLaHei",type = BlackManSqlProvider.class)
    Boolean deleteABlackMan(BlackMan sb);
}
