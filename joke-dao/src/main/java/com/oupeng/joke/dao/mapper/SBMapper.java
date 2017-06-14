package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.SBSqlProvider;
import com.oupeng.joke.domain.user.SB;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by Jane on 17-6-14.
 */
public interface SBMapper {

    @SelectProvider(method = "getASBStoBeLaHei", type = SBSqlProvider.class)
    @ResultType(value=SB.class)
    SB getASB(String username);

    @SelectProvider(method = "getSBStoBeLaHei", type = SBSqlProvider.class)
    @ResultType(value=SB.class)
    List<SB> getAllSBs();

    @InsertProvider(method="insertSBtoBeLaHei",type = SBSqlProvider.class)
    Boolean insertASB(SB sb);

    @DeleteProvider(method="deleteSBtoBeLaHei",type = SBSqlProvider.class)
    Boolean deleteASB(SB sb);
}
