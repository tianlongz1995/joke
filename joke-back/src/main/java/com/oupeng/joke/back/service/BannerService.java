package com.oupeng.joke.back.service;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.BannerMapper;
import com.oupeng.joke.dao.mapper.DistributorsMapper;
import com.oupeng.joke.domain.Banner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class BannerService {

    @Autowired
    private Environment env;
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private DistributorsMapper distributorsMapper;

    /**
     * 新增banner
     * @param title
     * @param img
     * @param cid
     * @param content
     * @param jid
     * @param type
     * @return
     */
    public boolean addBanner(String title,String img, Integer cid,String content,Integer jid,Integer type,Integer adid){
        Banner banner = new Banner();
        banner.setContent(content);
        //内容上传图片
        banner.setImg(img);
        banner.setTitle(title);
        banner.setCid(cid);
        banner.setJid(jid);
        banner.setType(type);
        banner.setAdid(adid);

        Integer sort = bannerMapper.getMaxSortByCid(cid);
        if( null != sort){
            banner.setSort(sort+1);

        } else{
            banner.setSort(1);
        }
        bannerMapper.addBanner(banner);
        return true;
    }


    /**
     * 统计banner列表总数
     * @param status
     * @param cid
     * @return
     */
    public Integer getBannerListCount(Integer status,Integer cid){
        return bannerMapper.getBannerListCount(status,cid);
    }

   public List<Banner> getBannerList(Integer status,Integer cid,Integer offset,Integer pageSize){
       List<Banner> bannerList = bannerMapper.getBannerList(status,cid, offset, pageSize);
       if(!CollectionUtils.isEmpty(bannerList)){
           for(Banner banner : bannerList){
               if(StringUtils.isNotBlank(banner.getImg())){
                   banner.setImg( env.getProperty("img.real.server.url") + banner.getImg());
               }
           }
       }
       return bannerList;
   }

    /**
     * 根据id获取banner
     * @param id
     * @return
     */
   public Banner getBannerById(Integer id){
       Banner banner = bannerMapper.getBannerById(id);
       if(banner != null && StringUtils.isNotBlank(banner.getImg())){
           banner.setImg(env.getProperty("img.real.server.url") + banner.getImg());
       }
       return banner;
   }

    /**
     * banner 更新
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
    public boolean updateBanner(Integer id, String title, Integer cid, String img, String content,Integer jid,Integer type,Integer adId) {
        Banner banner = new Banner();
        banner.setTitle(title);
        banner.setId(id);
        banner.setCid(cid);
        //TODO 禁止图片处理
        //内容上传图片
        banner.setImg(img);
        banner.setContent(content);
        banner.setJid(jid);
        banner.setType(type);
        banner.setAdid(adId);
        bannerMapper.updateBanner(banner);
        return true;
    }

//    public String handleImg(String imgUrl){
//        if(StringUtils.isNotBlank(imgUrl)){
//            ImgRespDto imgRespDto = HttpUtil.handleImg(env.getProperty("remote.crop.img.server.url"),imgUrl, false);
//            if(imgRespDto != null && imgRespDto.getErrorCode() == 0){
//                return imgRespDto.getImgUrl();
//            }
//        }
//        return null;
//    }

    /**
     * 删除banner
     * @param id
     * @param cid
     */
    public void delBanner(Integer id,Integer cid) {
        //更新排序值
        List<Banner> list = bannerMapper.getBannerMoveList(cid);
        //删除元素index
        int position = -1;
        //banner大于1个,且删除的不是最后一个元素,更新排序值,12345 删除 3 -》1234
        if (list.size() > 1 && id != list.get(list.size() - 1).getId()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == id) {
                    position = i;
                }
            }
            for (int i = position; i < list.size() - 1; i++) {
                //当前banner排序值
                int sort = list.get(i).getSort();
                bannerMapper.updateBannerSort(list.get(i + 1).getId(), sort);
            }
        }
        //删除记录
        bannerMapper.delBanner(id);
    }

    /**
     * 更新banner状态
     * @param id
     * @param status 0 下线 1上线
     * @return
     */
    public boolean updateBannerStatus(Integer id, Integer status) {
        Banner banner = bannerMapper.getBannerById(id);
        String bannerKey = JedisKey.STRING_BANNER + id;
        String bannerListKey = JedisKey.JOKE_BANNER + banner.getCid();
        // 下线删除缓存
        if (status == 0) {
            //更新状态
            bannerMapper.updateBannerStatus(id, status);
            jedisCache.del(bannerKey);
            jedisCache.zrem(bannerListKey,Integer.toString(id));
        } else {
            // 判断上线banner个数
            Integer bannerCount = bannerMapper.getBannerListCount(1,banner.getCid());
            if (bannerCount >= 5) {
                return false;
            }
            // 上线增加缓存
            bannerMapper.updateBannerStatus(id, status);
            jedisCache.set(bannerKey, JSON.toJSONString(banner));
            jedisCache.zadd(bannerListKey,System.currentTimeMillis(),Integer.toString(id));
        }
        //修改channel中banner状态
       Long bannerCount = jedisCache.zcard(bannerListKey);
//        （0：不显示、1：显示）
        if(bannerCount == 0){
            distributorsMapper.updateChannelsBanner(0,banner.getCid());
        }else{
            distributorsMapper.updateChannelsBanner(1,banner.getCid());
        }
      return  true;
    }

    /**
     * banner 移动
     * @param id 频道id
     * @param type 1
     * @param sort
     * @return
     */
    public boolean bannerMove(Integer id, Integer cid,Integer type, Integer sort) {
        //获取频道下banner
        List<Banner> list = bannerMapper.getBannerMoveList(cid);
        if(!CollectionUtils.isEmpty(list)){
            Banner d = list.get(0);
            //第一个元素禁止上移
            if(d.getId() == id && type ==1){
                return false;
            }
            if(type == 1){// 上移
                int lastIndex = 0;
                int lastId = 0;
                for(Banner b : list){
                    if(b.getId() == id ){
                        break;
                    }
                    lastIndex = b.getSort();
                    lastId    = b.getId();
                }
                bannerMapper.updateBannerSort(id, lastIndex);
                bannerMapper.updateBannerSort(lastId, sort);
                return true;
            } else if(type == 2){// 下移
                int lastIndex = 0;
                int lastId = 0;
                int end = 1;
                for(Banner b : list){
                    lastIndex = b.getSort();
                    lastId = b.getId();
                    if(end == 0){
                        break;
                    }
                    if(b.getId() == id){
                        end--;
                    }
                }
                bannerMapper.updateBannerSort(id, lastIndex);
                bannerMapper.updateBannerSort(lastId, sort);
                return true;
            }
        }
        return false;
    }
}
