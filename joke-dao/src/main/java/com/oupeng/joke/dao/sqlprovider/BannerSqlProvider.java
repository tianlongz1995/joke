package com.oupeng.joke.dao.sqlprovider;

import com.oupeng.joke.domain.Banner;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by rainy on 2016/12/30.
 */
public class BannerSqlProvider {

    /**
     * 新增Banner
     *
     * @param banner
     * @return
     */
    public static String addBanner(Banner banner) {
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into banner(title, img, cid,did,status,content,jid,type,slot,sort,width,height,publish_time,create_time, update_time) value (");
        if (StringUtils.isNotBlank(banner.getTitle())) {
            sql.append("'").append(banner.getTitle().trim()).append("',");
        } else {
            sql.append("null,");
        }
        //img
        if (StringUtils.isNotBlank(banner.getImg())) {
            sql.append("'").append(banner.getImg()).append("',");
        } else {
            sql.append("null,");
        }
        //cid
        if (StringUtils.isNotBlank(banner.getCid().toString())) {
            sql.append("'").append(banner.getCid()).append("',");
        } else {
            sql.append("null,");
        }
        if(banner.getDid()!=null){
            sql.append(banner.getDid()).append(",");
        }else{
            sql.append("2,");
        }
        //status
        sql.append("0,");
        //content
        if (StringUtils.isNotBlank(banner.getContent())) {
            sql.append("'").append(banner.getContent().trim()).append("',");
        } else {
            sql.append("null,");
        }
        //jid
        if (null != banner.getJid()) {
            sql.append("'").append(banner.getJid()).append("',");
        } else {
            sql.append("null,");
        }
        //type 内容类型
        if (null != banner.getType()) {
            sql.append("'").append(banner.getType()).append("',");
        } else {
            sql.append("null,");
        }
        //adid 广告位id
        if (null != banner.getSlot()) {
            sql.append("'").append(banner.getSlot()).append("',");
        } else {
            sql.append("null,");
        }
        //sort 排序值
        if (null != banner.getSort()) {
            sql.append("'").append(banner.getSort()).append("',");
        } else {
            sql.append("null,");
        }

        if (null != banner.getWidth()) {
            sql.append(banner.getWidth()).append(",");
        } else {
            sql.append("0,");
        }
        if (null != banner.getHeight()) {
            sql.append(banner.getHeight()).append(",");
        } else {
            sql.append("0,");
        }
        if(null!=banner.getPublishTime()){
            sql.append("'").append(banner.getPublishTimeString()).append("', ");
        }else{
            sql.append("null, ");
        }
        sql.append("now(),now())");
        return sql.toString();
    }

    /**
     * 统计banner 列表总数
     *
     * @param map
     * @return
     */
    public static String getBannerListCount(Map<String, Object> map) {
        Object status = map.get("status");
        Object cid = map.get("cid");
        Object did = map.get("did");
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) from banner where 1 = 1 ");
        if (status != null) {
            sql.append(" and status = ").append(status);
        } else {
            sql.append(" and status != 4 ");
        }
        if (cid != null) {
            sql.append(" and cid = ").append(cid);
        }
        if(did  != null){
            sql.append(" and did = ").append(did);
        }
        return sql.toString();
    }

    /**
     * 获取banner列表
     *
     * @param map
     * @return
     */
    public static String getBannerList(Map<String, Object> map) {
        Object status    = map.get("status");
        Object cid       = map.get("cid");
        Object did       = map.get("did");
        Object offset    = map.get("offset");
        Object pageSize  = map.get("pageSize");
        StringBuffer sql = new StringBuffer();
        sql.append("select b.id,b.title,b.jid,b.img,b.cid,b.did,d.name as dName,b.type,b.status,b.content,b.slot,b.sort,b.create_time as createTime,b.update_time as updateTime,b.publish_time as publishTime from banner b ");
        sql.append(" left join `distributors` d on b.did = d.id where 1 = 1 ");
        if(status != null){
            sql.append(" and b.status = ").append(status);
        } else {
            sql.append(" and b.status != 4 ");
        }
        if(null != cid){
            sql.append(" and b.cid = ").append(cid);
        }
        if(null != did){
            sql.append(" and b.did = ").append(did);
        }
        sql.append(" order by b.sort asc ");
        if(offset != null && pageSize != null){
            sql.append(" limit ").append(offset).append(" , ").append(pageSize);
        }
        return sql.toString();
    }

    /**
     * 更新banner
     * @param banner
     * @return
     */
    public static  String updateBanner(Banner banner){
        StringBuffer sql = new StringBuffer();
        sql.append(" update banner set update_time=now(),title = ");

        if(StringUtils.isNotBlank(banner.getTitle())){
            sql.append("'").append(banner.getTitle().trim()).append("',");
        }else{
            sql.append("null,");
        }
        sql.append(" cid=");
        if(null != banner.getCid()){
            sql.append("'").append(banner.getCid()).append("',");
        }else{
            sql.append("null,");
        }
        sql.append(" did=");
        if(null != banner.getDid()){
            sql.append(banner.getDid()).append(",");
        }else{
            sql.append("null,");
        }
        sql.append(" jid=");
        if(null !=banner.getJid()){
            sql.append("'").append(banner.getJid()).append("',");
        }else{
            sql.append("null,");
        }

        sql.append(" slot=");
        if(null !=banner.getSlot()){
            sql.append("'").append(banner.getSlot()).append("',");
        }else{
            sql.append("null,");
        }

        sql.append(" type=");
        if(StringUtils.isNotBlank(banner.getType().toString())){
            sql.append("'").append(banner.getType()).append("',");
        }else{
            sql.append("null,");
        }
        sql.append(" img=");
        if(StringUtils.isNotBlank(banner.getImg())){
            sql.append("'").append(banner.getImg()).append("',");
        }else{
            sql.append("null,");
        }
        sql.append(" publish_time=");
        if(null !=banner.getPublishTime()){
            sql.append("'").append(banner.getPublishTimeString()).append("', ");
        }else{
            sql.append("null, ");
        }
        if(null != banner.getWidth()){
            sql.append(" width = '").append(banner.getWidth()).append("', ");
        }
        if(null != banner.getHeight()){
            sql.append(" height = '").append(banner.getHeight()).append("', ");
        }
        sql.append(" content=");
        if(StringUtils.isNotBlank(banner.getContent())){
            sql.append("'").append(banner.getContent().trim()).append("' ");
        }else{
            sql.append("null");
        }

        sql.append(" where id = ").append(banner.getId());
        return sql.toString();
    }
}
