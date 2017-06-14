package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.SBSqlProvider;
import com.oupeng.joke.domain.user.BlackMan;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by Jane on 17-6-14.
 */
public interface BlackManMapper {

    @SelectProvider(method = "getASBStoBeLaHei", type = SBSqlProvider.class)
    int getABlackMan(String id);

    @SelectProvider(method = "getSBStoBeLaHei", type = SBSqlProvider.class)
    @ResultType(value=BlackMan.class)
    List<BlackMan> getAllBlackMans();

    @InsertProvider(method="insertSBtoBeLaHei",type = SBSqlProvider.class)
    int insertABlackMan(BlackMan sb);

    @DeleteProvider(method="deleteSBtoBeLaHei",type = SBSqlProvider.class)
    Boolean deleteABlackMan(BlackMan sb);
}
