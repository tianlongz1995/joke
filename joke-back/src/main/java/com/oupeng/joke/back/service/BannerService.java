package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.Im4JavaUtils;
import com.oupeng.joke.back.util.ImageUtils;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.BannerMapper;
import com.oupeng.joke.dao.mapper.DistributorsMapper;
import com.oupeng.joke.domain.*;
import com.oupeng.joke.domain.response.*;
import com.oupeng.joke.domain.response.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class BannerService {
    private static final Logger log = LoggerFactory.getLogger(BannerService.class);

    @Autowired
    private Environment env;
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private DistributorsMapper distributorsMapper;
    @Autowired
    private IndexCacheFlushService indexCacheFlushService;



    private String uploadPath = "/nh/java/back/resources/image/";
    private String showPath = "http://joke2admin.oupeng.com/resources/image/";
    private String realPath = "http://joke2-img.oupeng.com/";
    private String cdnPath = "/data01/images/";

    @PostConstruct
    public void initPath() {
        String u = env.getProperty("upload_image_path");
        String s = env.getProperty("show_image_path");
        String r = env.getProperty("img.real.server.url");
        String c = env.getProperty("img.cdn.path");
        if(StringUtils.isNotEmpty(u)){
            uploadPath = u;
        }
        if(StringUtils.isNotEmpty(s)){
            showPath = s;
        }
        if(StringUtils.isNotEmpty(r)){
            realPath = r;
        }
        if(StringUtils.isNotEmpty(c)){
            cdnPath = c;
        }
    }


    /**
     * 新增banner
     *
     * @param title
     * @param img
     * @param cid
     * @param content
     * @param jid
     * @param type
     * @return
     */
    public boolean addBanner(String title, String img, Integer cid, String content, Integer jid, Integer type, Integer adid, Integer width, Integer height,String publishTime,Integer[] did) {
        try {
            String fileName = HttpUtil.getUrlImageFileName(img);
            File file = new File(uploadPath + fileName);
            if(!file.exists()){
                log.error("文件不存在:[{}]!", file.toString());
                return false;
            }
            Banner banner = new Banner();
            banner.setContent(content);

            int random = new Random().nextInt(3000);
            File dir = new File(cdnPath + random);
            if(!dir.isDirectory()){
                dir.mkdirs();
            }
            String newFile = dir.getCanonicalPath() + "/" + fileName;
            //内容上传图片
            boolean result = Im4JavaUtils.copyImage(file.getCanonicalPath(), newFile);
            if (!result) {
                log.error("拷贝图片[{}]到目[{}]失败!");
                return false;
            }
            if(did == null || did.length < 1){
                log.error("新增横幅失败, 渠道编号为空!");
                return false;
            }

            Image image = ImageUtils.getImgWidthAndHeight(file);
            banner.setImg(newFile.replace(cdnPath, ""));
            banner.setTitle(title);
            banner.setCid(cid);
//            banner.setDid(did);
            banner.setJid(jid);
            banner.setType(type);
            banner.setSlot(adid);
            //新建banner，设置sort值为0
            banner.setSort(0);
            banner.setWidth(image.getWidth());
            banner.setHeight(image.getHeight());
            banner.setPublishTimeString(publishTime);
            bannerMapper.addBanner(banner);

//            添加渠道横幅关联
            bannerMapper.addDistributorBanner(banner.getId(), did);

            return true;
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return false;
        }
    }


    /**
     * 统计banner列表总数
     *
     * @param status
     * @param cid
     * @return
     */
    public Integer getBannerListCount(Integer status, Integer cid,Integer did) {
        return bannerMapper.getBannerListCount(status, cid, did);
    }

    /**
     * 查询banner列表
     * @param status 0 新建 1下线 2上线 3 已发布
     * @param cid    频道id
     * @param offset
     * @param pageSize
     * @return
     */
    public List<Banner> getBannerList(Integer status, Integer cid,Integer did, Integer offset, Integer pageSize) {
        try {
            List<Banner> bannerList = bannerMapper.getBannerList(status, cid, did, offset, pageSize);
            if (!CollectionUtils.isEmpty(bannerList)) {
                for (Banner banner : bannerList) {
                    if (StringUtils.isNotBlank(banner.getImg())) {
                        banner.setImg(realPath + banner.getImg());
                    }
                }
            }
            return bannerList;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return null;
        }

    }

    /**
     * 根据id获取banner
     *
     * @param id
     * @return
     */
    public Banner getBannerById(Integer id) {
        Banner banner = bannerMapper.getBannerById(id);
        if (banner != null && StringUtils.isNotBlank(banner.getImg())) {
            banner.setImg(realPath + banner.getImg());
        }
        return banner;
    }

    /**
     * banner 更新
     *
     * @param id
     * @param title
     * @param cid
     * @param img
     * @param content
     * @param jid
     * @param type
     * @param adId
     * @return
     */
    public boolean updateBanner(Integer id, String title, Integer cid, String img, String content, Integer jid, Integer type, Integer adId,String publishTime,Integer[] did) {
        try {
            Banner banner = new Banner();
            banner.setTitle(title);
            banner.setId(id);
            banner.setCid(cid);
//            banner.setDid(did);
            // 重新上传的图片
            if (img.startsWith(showPath)) {
                String fileName = HttpUtil.getUrlImageFileName(img);
                File file = new File(uploadPath + fileName);
                if (!file.exists()) {
                    log.error("文件不存在:[{}]!", file.toString());
                    return false;
                }
                int random = new Random().nextInt(3000);
                File dir = new File(cdnPath + random);
                if (!dir.isDirectory()) {
                    dir.mkdirs();
                }
                String newFile = dir.getCanonicalPath() + fileName;
                //内容上传图片
                boolean result = Im4JavaUtils.copyImage(file.getCanonicalPath(), newFile);
                if (!result) {
                    log.error("拷贝图片[{}]到目[{}]失败!");
                    return false;
                }
                banner.setImg(newFile.replace(cdnPath, ""));
                Image image = ImageUtils.getImgWidthAndHeight(file);
                banner.setWidth(image.getWidth());
                banner.setHeight(image.getWidth());
            } else {
                //已经上传的图片
                img = img.replace(realPath, "");
                banner.setImg(img);
            }
            banner.setContent(content);
            banner.setJid(jid);
            banner.setType(type);
            banner.setSlot(adId);
            banner.setPublishTimeString(publishTime);
        //  更新横幅
            bannerMapper.updateBanner(banner);
        //  删除渠道横幅关联关系
            bannerMapper.delDistributorsBanners(id);
        //  添加新的渠道横幅关联关系
            bannerMapper.addDistributorBanner(id, did);

            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

//    public Image handleImg(String imgUrl) {
//        if (StringUtils.isNotBlank(imgUrl)) {
//            Im4JavaUtils.
//            ImgRespDto imgRespDto = HttpUtil.handleImg(cropPath, imgUrl, false);
//            log.debug("获取图片处理返回结果:{}", JSON.toJSONString(imgRespDto));
//            if (imgRespDto != null && imgRespDto.getErrorCode() == 0) {
//                return imgRespDto.getImgUrl();
//            }
//        }
//        return null;
//    }

    /**
     * 删除横幅
     *
     * @param id
     */
    public void delBanner(Integer id) {
        //删除横幅记录
        bannerMapper.delBanner(id);
//        删除横幅渠道关联关系
        bannerMapper.delDistributorsBanners(id);
    }

    /**
     * 更新banner状态
     * 校验是否能发布
     * @param id
     * @param status 0 新建 1 下线 2上线 3 已发布 ,
     * @return
     */
    public String publishBannerByTime(Integer id, Integer status) {
        String result = null;
        Banner banner = bannerMapper.getBannerById(id);
        String bannerKey = JedisKey.STRING_BANNER + id;
        String bannerListKey = JedisKey.JOKE_BANNER + banner.getDid()+"_"+banner.getCid();
        //上线
        if (Constants.BANNER_STATUS_VALID == status) {
            result = validBanner(bannerMapper.getBannerById(id),true);
        } else if (Constants.TOPIC_STATUS_PUBLISH != status) { //下线
            //删除缓存
            jedisCache.del(bannerKey);
            jedisCache.zrem(bannerListKey, Integer.toString(id));
            //置排序值为0
//            bannerMapper.updateBannerSort(id, 0);
        }
        if (result == null) { //验证通过，或者下线，更新状态
            //上线操作，修改sort值
            if(Constants.BANNER_STATUS_VALID==status){
                //1.修改排序值
                Integer maxScore = bannerMapper.getMaxSortByCidAndDid(banner.getCid(),banner.getDid());
                if (null == maxScore) {
                    bannerMapper.updateBannerSort(id, 1);
                } else {
                    bannerMapper.updateBannerSort(id, maxScore + 1);
                }
            }
            bannerMapper.updateBannerStatus(id, status);
        }
        //根据缓存中banner的个数，修改channel表中banner状态
        Long bannerCount = jedisCache.zcard(bannerListKey);
        //（0：不显示、1：显示）
        if (bannerCount == 0) {
            distributorsMapper.updateChannelsBanner(0, banner.getCid());
            changeBannerStatus(banner.getCid(), banner.getDid(), false);
        } else {
            distributorsMapper.updateChannelsBanner(1, banner.getCid());
            if(bannerCount == 1){
//                修改渠道配置中banner显示状态
                changeBannerStatus(banner.getCid(), banner.getDid(), true);
            }
        }
        return result;
    }

    /**
     * banner的立即发布
     * @param id
     * @return
     */
    public String publishBannerNow(Integer id){
        String result;
//        查询有几个渠道需要发布
        List<Banner> list = bannerMapper.getDistributorsBannersList(id);


        Banner banner = bannerMapper.getBannerById(id);
        String bannerKey = JedisKey.STRING_BANNER + id;
        String bannerListKey = JedisKey.JOKE_BANNER + banner.getDid()+"_"+banner.getCid();
        result = validBanner(banner,false);
        if(null == result){ //验证通过
            //1 增加缓存
            jedisCache.set(bannerKey, JSON.toJSONString(banner));
            jedisCache.zadd(bannerListKey, System.currentTimeMillis(), Integer.toString(id));

            //已发布的banner的最大排序值
            Integer maxScore = bannerMapper.getMaxSortByCidAndDid(banner.getCid(),banner.getDid());
            //更改立即发布的banner的排序值 maxScore+1
            if (null == maxScore) {
                bannerMapper.updateBannerSort(id, 1);
            } else {
                bannerMapper.updateBannerSort(id, maxScore + 1);
            }
            //修改所有待发布的banner的排序值+1
            List<Banner> bannerList = bannerMapper.getBannerList(2,banner.getCid(),banner.getDid(),0,5);
            if (!CollectionUtils.isEmpty(bannerList)) {
                for (Banner b:bannerList){
                bannerMapper.updateBannerSort(b.getId(),b.getSort()+1);
                }
            }
            //2 修改发布状态
            bannerMapper.updateBannerStatus(banner.getId(),3);
//          修改Channel表显示状态
            distributorsMapper.updateChannelsBanner(1, banner.getCid());

            //根据缓存中banner的个数，修改channel表中banner状态
            Long bannerCount = jedisCache.zcard(bannerListKey);
            if(bannerCount == 1){
//                修改渠道配置中banner显示状态
                changeBannerStatus(banner.getCid(), banner.getDid(), true);
            }

        }
        return result;
    }

    /**
     * 修改渠道配置中banner显示状态
     * @param cid
     * @param bannerView
     */
    public void changeBannerStatus(Integer cid, Integer did, boolean bannerView) {
        String configStr = jedisCache.hget(JedisKey.JOKE_DISTRIBUTOR_CONFIG, String.valueOf(did));
        log.info("修改渠道[{}]-[{}]-[{}]配置前:[{}]", did, cid, bannerView, configStr);
        DistributorsConfig distributorsConfig = JSON.parseObject(configStr, DistributorsConfig.class);
        boolean change = false;
        if(distributorsConfig != null){
            List<Channels> channelsList = distributorsConfig.getChannels();
            if(!CollectionUtils.isEmpty(channelsList)){
                for(Channels c : channelsList){
                    if(c.getI().equals(cid)){
                        c.setB(bannerView);
                        log.info("修改渠道[{}]配置中频道[{}]banner显示状态[{}]", did, cid, bannerView);
                        change = true;
                        break;
                    }
                }
            }
        }
        if(change){
//          更新缓存状态
            jedisCache.hset(JedisKey.JOKE_DISTRIBUTOR_CONFIG, String.valueOf(did), JSON.toJSONString(distributorsConfig));
//		    更新前台应用内缓存
            indexCacheFlushService.updateIndex(new IndexItem(String.valueOf(did), 0));
        }
        log.info("修改渠道配置[{}]后:[{}]", change, JSON.toJSONString(distributorsConfig));
    }

    /**
     * 验证banner是否能正常发布
     * @param banner
     * @return
     */
    private String validBanner(Banner banner,boolean validPublishTime){
        // 判断发布上线banner数量
        Integer bannerCount = bannerMapper.getBannerCount(banner.getDid(),banner.getCid());
        if (bannerCount >= 5) {
            return "上线的banner不能超过5个";
        }
        if(validPublishTime) {
            if (banner.getPublishTime() == null) {
                return "banner的发布时间不能为空";
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            if(banner.getPublishTime().compareTo(calendar.getTime()) < 0) {
                return "发布时间最少要在下一个小时";
            }
        }
        if(StringUtils.isBlank(banner.getImg())&& banner.getType()==0){
            return "banner为内容时,图片不能为空";
        } else if (null == banner.getJid() && banner.getType() == 0) {
            return "banner为内容时，段子id不能为空";
        }else if(null == banner.getSlot()&&banner.getType() == 1 ){
            return "banner为广告时,广告位不能为空";
        }
        return null;
    }



    /**
     * banner 移动 ，仅在上线的banner中有效
     *
     * @param id   频道id
     * @param type 1
     * @param sort
     * @return
     */
    public boolean bannerMove(Integer id, Integer cid,Integer did, Integer type, Integer sort) {
        //获取频道下 ，已上线的banner
        List<Banner> list = bannerMapper.getBannerMoveList(cid,did);
        if (!CollectionUtils.isEmpty(list)) {
            Banner d = list.get(0);
            //第一个元素禁止上移
            if (d.getId().equals(id) && type == 1) {
                return false;
            }
            // 1.更新排序值
            if (type == 1) {// 上移
                int lastIndex = 0;
                int lastId = 0;
                for (Banner b : list) {
                    if (b.getId().equals(id)) {
                        break;
                    }
                    lastIndex = b.getSort();
                    lastId = b.getId();
                }
                bannerMapper.updateBannerSort(id, lastIndex);
                bannerMapper.updateBannerSort(lastId, sort);
            } else if (type == 2) {// 下移
                int lastIndex = 0;
                int lastId = 0;
                int end = 1;
                for (Banner b : list) {
                    lastIndex = b.getSort();
                    lastId = b.getId();
                    if (end == 0) {
                        break;
                    }
                    if (b.getId().equals(id)) {
                        end--;
                    }
                }
                bannerMapper.updateBannerSort(id, lastIndex);
                bannerMapper.updateBannerSort(lastId, sort);
            }
            //2.更新缓存,重置
            List<Banner> listUpdated = bannerMapper.getBannerMoveList(cid,did);
            if (!CollectionUtils.isEmpty(listUpdated)) {
                for (Banner b : listUpdated) {
                    jedisCache.set(JedisKey.STRING_BANNER + b.getId(), JSON.toJSONString(b));
                }
                return true;
            }
        }
        return false;
    }


    /**
     * 获取待发布的专题
     * @return
     */
    public List<Banner> getBannerForPublish(){
        return bannerMapper.getBannerForPublish();
    }


    /**
     * 获取渠道编号和名称
     * @return
     */
    public List<Distributor> getDistributorIdAndName(){
        return  distributorsMapper.getDistributorIdAndName();
    }

    /**
     * 获取已配置横幅的渠道编号列表
     * @param bannerId
     * @return
     */
    public List<Integer> getDistributorsBanners(Integer bannerId) {
        return bannerMapper.getDistributorsBanners(bannerId);
    }

    /**
     * 获取渠道列表
     * @param id
     * @return
     */
    public List<Distributor> distributorList(Integer id) {
        return bannerMapper.distributorList(id);
    }

    /**
     * 修改排序值
     * @param ids
     * @param sorts
     * @param username
     * @return
     */
    public Result editSorts(Integer[] ids, Integer[] sorts, String username) {
        if(ids == null || ids.length < 1 || sorts == null || ids.length != sorts.length){
            return new Failed("参数错误!");
        }
        int count = 0;
        for(int i = 0; i < ids.length; i++){
            bannerMapper.editSort(ids[i], sorts[i], username);
            Banner banner = bannerMapper.getBannerByDbId(ids[i]);
//          更新缓存排序值
            String key = JedisKey.STRING_BANNER + banner.getId();
            if(banner != null){
                banner.setSort(sorts[i]);
                jedisCache.set(key, JSON.toJSONString(banner));
            }
            count++;
        }

        if(count > 0){
            return new Success("修改成功!");
        } else {
            return new Failed("["+ JSON.toJSONString(ids)+"]修改排序值["+JSON.toJSONString(sorts)+"]失败!");
        }
    }
}
