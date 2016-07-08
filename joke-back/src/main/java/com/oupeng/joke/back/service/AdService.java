package com.oupeng.joke.back.service;

import com.oupeng.joke.dao.mapper.AdMapper;
import com.oupeng.joke.domain.Ad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广告服务
 * Created by hushuang on 16/7/6.
 */
@Service
public class AdService {
    private static final Logger logger = LoggerFactory.getLogger(AdService.class);
    @Autowired
    private AdMapper adMapper;

    /**
     * 获取广告列表记录总数
     * @param ad
     * @return
     */
    public int getAdListCount(Ad ad) {
        return adMapper.getAdListCount(ad);
    }
    /**
     * 获取广告列表
     * @param ad        广告
     * @return
     */
    public List<Ad> getAdList(Ad ad){
        return adMapper.getAdList(ad);
    }

    /**
     * 获取广告信息
     * @param id 广告编号
     * @return
     */
    public Ad getAdById(Integer id){
        return adMapper.getAdById(id);
    }

    /**
     * 修改状态
     * @param id
     * @param status
     */
    public void updateAdStatus(Integer id,Integer status){
        adMapper.updateAdStatus(id, status);
    }

    /**
     * 新增广告
     * @param ad
     */
    public void insertAd(Ad ad){
        adMapper.insertAd(ad);
    }

    /**i
     * 更新广告信息
     * @param id
     * @param slotId
     * @param pos
     * @param slide
     * @param did
     * @param status
     */
    public void updateAd(Integer id, Integer slotId, Integer pos, Integer slide, Integer did, Integer status){
        Ad ad = new Ad();
        ad.setId(id);
        ad.setSlotId(slotId);
        ad.setPos(pos);
        ad.setSlide(slide);
        ad.setDid(did);
        ad.setStatus(status);
        adMapper.updateAd(ad);
    }

}
