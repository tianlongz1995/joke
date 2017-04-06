package com.oupeng.joke.back.controller;


import com.oupeng.joke.back.service.BannerService;
import com.oupeng.joke.domain.Banner;
import com.oupeng.joke.domain.Comment;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/banner")
public class BannerController {


    @Autowired
    private BannerService bannerService;

    /**
     * 获取banner列表
     *
     * @param status     状态
     * @param cid        频道id
     * @param pageNumber 页码
     * @param pageSize   页面显示条数
     * @param model
     * @return
     */
    @RequestMapping(value = "/list")
    public String getBannerList(@RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "cid", required = false) Integer cid,
                                @RequestParam(value = "did", required = false) Integer did,
                                @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                Model model) {
        pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
        pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
        int pageCount = 0;//总页数
        int offset;//开始条数index
        List<Banner> list = null;
        List<Distributor> distributorList = null;
        int count = bannerService.getBannerListCount(status, cid, did);//总条数
        if (count > 0) {
            if (count % pageSize == 0) {
                pageCount = count / pageSize;
            } else {
                pageCount = count / pageSize + 1;
            }

            if (pageNumber > pageCount) {
                pageNumber = pageCount;
            }
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            offset = (pageNumber - 1) * pageSize;

            list = bannerService.getBannerList(status, cid, did, offset, pageSize);
        }
        distributorList = bannerService.getDistributorIdAndName();
        model.addAttribute("count", count);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("list", list);
        model.addAttribute("distributor",distributorList);
        model.addAttribute("status", status);
        model.addAttribute("cid", cid);
        model.addAttribute("did",did);
//        if (list != null){
//            model.addAttribute("firstElement",bannerService.getBannerList(status, cid,did, 0, 1).get(0).getId());
//        }else{
//            model.addAttribute("firstElement","");
//        }
        return "/banner/list";
    }

    /**
     * 新增banner
     * @param title
     * @param img
     * @param cid
     * @param content
     * @param jid
     * @param type
     * @param adid
     * @param width
     * @param height
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public Result addBanner(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "img", required = false) String img,
                            @RequestParam(value = "cid") Integer cid,
                            @RequestParam(value = "content") String content,
                            @RequestParam(value = "jid")  Integer jid,
                            @RequestParam(value = "type") Integer type,
                            @RequestParam(value = "adid") Integer adid,
                            @RequestParam(value = "publishTime",required = false) String publishTime,
                            @RequestParam(value = "width",required = false) Integer width,
                            @RequestParam(value = "height",required = false) Integer height,
                            @RequestParam(value = "did") Integer[] did) {
        boolean result = bannerService.addBanner(title, img, cid, content, jid, type, adid, width, height, publishTime, did);
        if (result) {
            return new Success("添加成功!");
        } else {
            return new Failed("添加失败!");
        }
    }

    /**
     * Banner 编辑页面
     * @param id         banner关联id
     * @param status     状态        跳回列表页参数
     * @param cid        频道id      跳回列表页参数
     * @param pageSize   页面显示条数  跳回列表页参数
     * @param pageNumber 页码        跳回列表页参数
     * @param model
     * @return
     */
    @RequestMapping(value = "edit")
    public String edit(@RequestParam(value = "id") Integer id,
                       @RequestParam(value = "status",required = false)     Integer status,
                       @RequestParam(value = "cid",required = false)        Integer cid,
                       @RequestParam(value = "pageSize",required = false)   Integer pageSize,
                       @RequestParam(value = "pageNumber",required = false) Integer pageNumber,
                       Model model) {
        List<Distributor> distributorList = bannerService.getDistributorIdAndName();
        Banner banner = bannerService.getBannerByDbId(id);
        model.addAttribute("banner", banner);
        model.addAttribute("cid", cid);
        model.addAttribute("did", bannerService.getDistributorsBanners(banner.getId()));
        model.addAttribute("status", status);
        model.addAttribute("distributor", distributorList);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        return "/banner/edit";
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
     * @return
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public Result update(@RequestParam(value = "id")      Integer id,
                         @RequestParam(value = "title")   String title,
                         @RequestParam(value = "cid")     Integer cid,
                         @RequestParam(value = "img")     String img,
                         @RequestParam(value = "content") String content,
                         @RequestParam(value = "jid")     Integer jid,
                         @RequestParam(value = "type")    Integer type,
                         @RequestParam(value = "publishTime") String publishTime,
                         @RequestParam(value = "adId")    Integer adId,
                         @RequestParam(value = "width",required = false) Integer width,
                         @RequestParam(value = "height",required = false) Integer height,
                         @RequestParam(value = "did") Integer[] did) {
        Banner banner = bannerService.getBannerByDbId(id);
        //下线和新建的的banner可以编辑
        if (banner.getStatus() == 0 || banner.getStatus()== 1) {
           boolean flag = bannerService.updateBanner(id, title, cid, img, content, jid, type, adId, publishTime, did);
            if(flag) {
                return new Success("更新成功!");
            }else{
                return new Failed("更新失败");
            }
        } else {
            return new Failed("更新失败!上线banner无法编辑");
        }
    }

    /**
     * 删除banner
     * @param id
     * @return
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public Result del(@RequestParam(value = "id") Integer id) {
        bannerService.delBanner(id);
        return new Success("删除成功!");
    }

    /**
     * 上线下线
     * 上线，1修改排序值，2增加缓存
     * 下线，1重新排序，2删除缓存
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "offlineOnline")
    @ResponseBody
    public Result offlineOnline(@RequestParam(value = "id")Integer id,
                                @RequestParam(value = "status") Integer status){
        String result;
        if (status == 4) { //立即发布
            result = bannerService.publishBannerNow(id);
        } else {
            result = bannerService.publishBannerByTime(id, status);
        }
        if (null == result) {
            return new Success();
        } else {
            return new Failed(result);
        }
    }

    /**
     * banner 移动
     * @param id
     * @param type
     * @param sort
     * @return
     */
    @RequestMapping(value = "bannerMove")
    @ResponseBody
    public Result bannerMove(@RequestParam(value = "id")   Integer id,
                             @RequestParam(value = "cid")  Integer cid,
                             @RequestParam(value = "type") Integer type,
                             @RequestParam(value = "sort") Integer sort,
                             @RequestParam(value = "did")  Integer did){
        Boolean flag = bannerService.bannerMove(id,cid,did,type,sort);
        if(flag){
            return new Success("banner移动成功");
        }else{
            return new Failed("banner移动失败");
        }
    }

    /**
     *  获取渠道列表
      * @param id
     * @return
     */
    @RequestMapping(value = "distributorList", produces = {"application/json"})
    @ResponseBody
    public Result distributorList(@RequestParam(value = "id")Integer id){
        List<Distributor> list = bannerService.distributorList(id);
        if(!CollectionUtils.isEmpty(list)){
            return new Success(list);
        }else{
            return new Failed("渠道列表获取失败!");
        }
    }

    /**
     * 修改排序值
     * @param ids
     * @param sorts
     * @return
     */
    @RequestMapping(value="/editSorts")
    @ResponseBody
    public Result editSorts(@RequestParam(value="ids")Integer[] ids,
                            @RequestParam(value="sorts")Integer[] sorts){
        String username = getUserName();
        if(username == null){
            return new Failed("登录信息失效,请重新登录!");
        }
        return bannerService.editSorts(ids, sorts, username);
    }



    /**
     * 获取当前登录用户名
     * @return
     */
    private String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails != null && userDetails.getUsername() != null){
            return userDetails.getUsername();
        }
        return null;
    }
}
