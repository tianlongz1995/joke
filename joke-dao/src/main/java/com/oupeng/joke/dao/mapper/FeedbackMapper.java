package com.oupeng.joke.dao.mapper;

import com.oupeng.joke.dao.sqlprovider.FeedbackSqlProvider;
import com.oupeng.joke.domain.LineDataSource;
import com.oupeng.joke.domain.PieGraph;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface FeedbackMapper {

	/**
	 * 查询渠道反馈数量列表
	 * @return
	 * @param startDate
	 * @param endDate
	 * @param distributorId
	 */
	@SelectProvider(method="getDistributorFeedbackCountList",type=FeedbackSqlProvider.class)
	List<PieGraph> getDistributorFeedbackCountList(@Param(value = "startDate")String startDate, @Param(value = "endDate")String endDate, @Param(value = "distributorId")Integer distributorId);

	/**
	 * 获取反馈类型列表
	 * @return
	 * @param startDate
	 * @param endDate
	 * @param distributorId
	 */
	@SelectProvider(method="getDistributorFeedbackTypeList",type=FeedbackSqlProvider.class)
	List<LineDataSource> getDistributorFeedbackTypeList(@Param(value = "startDate")String startDate, @Param(value = "endDate")String endDate, @Param(value = "distributorId")Integer distributorId);
}
