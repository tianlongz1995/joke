package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.BlackManSqlProvider;
import com.oupeng.joke.domain.user.BlackMan;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Jane on 17-6-14.
 */
public interface BlackManMapper {

    @SelectProvider(method = "countBlackMantoBeLaHei", type = BlackManSqlProvider.class)
    int getABlackMan(String id);

    @SelectProvider(method = "countBLackManstoBeLaHei", type = BlackManSqlProvider.class)
    int countBlackMan();

    @SelectProvider(method = "listBlackMansinRange", type = BlackManSqlProvider.class)
    @ResultType(BlackMan.class)
    List<BlackMan> listBlackManInRange(@Param(value = "offset") int offset, @Param(value = "pageSize") Integer pageSize);

    @SelectProvider(method = "getBlackMantoBeLaHei", type = BlackManSqlProvider.class)
    @ResultType(BlackMan.class)
    List<BlackMan> getBlackMan(String id);

    @SelectProvider(method = "listBlackMantoBeLaHei", type = BlackManSqlProvider.class)
    @ResultType(BlackMan.class)
    List<BlackMan> listBlackMan();

    @InsertProvider(method = "insertABlackMantoBeLaHei", type = BlackManSqlProvider.class)
    int insertABlackMan(BlackMan sb);

    @DeleteProvider(method = "deleteBlackMantoBeLaHei", type = BlackManSqlProvider.class)
    Boolean deleteABlackMan(String uid);
}
