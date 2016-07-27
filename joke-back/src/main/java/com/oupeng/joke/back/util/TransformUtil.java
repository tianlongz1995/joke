package com.oupeng.joke.back.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.oupeng.joke.domain.Channel;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.statistics.TimeDetail;
import com.oupeng.joke.domain.statistics.TimeDetailExport;

public class TransformUtil {

	public static void exec(TimeDetail timeDetail,TimeDetailExport export){
		export.setTime(String.valueOf(timeDetail.getTime()));
		export.setDistributorName(timeDetail.getDid());
		String cid = timeDetail.getCid();
		if(StringUtils.isNotBlank(cid)){
			export.setChannelName(cid);
		}
		export.setTotalPv(String.valueOf(timeDetail.getTotalPv()));
		export.setTotalUv(String.valueOf(timeDetail.getTotalUv()));
		export.setPerPv(FormatUtil.getRoundHalfUp4String(timeDetail.getTotalPv(), timeDetail.getTotalUv()));
		export.setEnterPv(String.valueOf(timeDetail.getEnterPv()));
		export.setEnterUv(String.valueOf(timeDetail.getEnterUv()));
		export.setPerEnterPv(FormatUtil.getRoundHalfUp4String(timeDetail.getEnterPv(), timeDetail.getEnterUv()));
		export.setListPv(String.valueOf(timeDetail.getListPv()));
		export.setListUv(String.valueOf(timeDetail.getListUv()));
		export.setPerListPv(FormatUtil.getRoundHalfUp4String(timeDetail.getListPv(), timeDetail.getListUv()));
		export.setDetailPv(String.valueOf(timeDetail.getDetailPv()));
		export.setDetailUv(String.valueOf(timeDetail.getDetailUv()));
		export.setPerDetailPv(FormatUtil.getRoundHalfUp4String(timeDetail.getDetailPv(), timeDetail.getDetailUv()));
		export.setNewUserPv(String.valueOf(timeDetail.getNewUserPv()));
		export.setNewUserUv(String.valueOf(timeDetail.getNewUserUv()));
		export.setNewUserKeep(String.valueOf(timeDetail.getNewUserKeep()));
		export.setNewUserKeepRate(FormatUtil.getRoundHalfUp4String(timeDetail.getNewUserKeep(), timeDetail.getLastNewUserUv()));
		export.setOldUserPv(String.valueOf(timeDetail.getOldUserPv()));
		export.setOldUserUv(String.valueOf(timeDetail.getOldUserUv()));
		export.setOldUserKeep(String.valueOf(timeDetail.getOldUserKeep()));
		export.setOldUserKeepRate(FormatUtil.getFormat100(timeDetail.getOldUserKeep(), timeDetail.getLastOldUserUv()));
		export.setActionUserKeep(String.valueOf(timeDetail.getActionUserKeep()));
		export.setActionUserKeepRate(FormatUtil.getFormat100(timeDetail.getActionUserKeep(), timeDetail.getLastActionUserUv()));
	}
	
	public static void exec(TimeDetailExport export,List<Distributor> distributorList,List<Channel> channelList){
		if(StringUtils.isNotBlank(export.getDistributorName())){
			for(Distributor distributor : distributorList){
				if(String.valueOf(distributor.getId()).equals(export.getDistributorName())){
					export.setDistributorName(distributor.getName());
					break;
				}
			}
		}
		
		if(StringUtils.isNotBlank(export.getChannelName())){
			for(Channel channel : channelList){
				if(String.valueOf(channel.getId()).equals(export.getChannelName())){
					export.setChannelName(channel.getName());
					break;
				}
			}
		}
	}
	
	public static String listToString(List<Integer> list){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		StringBuffer str = new StringBuffer();
		for(Integer i : list){
			str.append("'").append(i).append("',");
		}
		return str.deleteCharAt(str.lastIndexOf(",")).toString();
	}
}
