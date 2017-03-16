package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.ChoiceService;
import com.oupeng.joke.domain.Choice;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/choice")
public class ChoiceController {

    @Autowired
    private ChoiceService choiceService;

    /**
     * 精选列表
     *
     * @param status
     * @param pageNumber
     * @param pageSize
     * @param model
     */
    @RequestMapping(value = "/list")
    public String getList(@RequestParam(value = "status", required = false) Integer status,
                          @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                          Model model) {
        pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
        pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
        int pageCount = 0;//总页数
        int offset;//开始条数index
        List<Choice> list = new ArrayList<>();
        int count = choiceService.getChoiceListCount(status);//总条数
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
            list = choiceService.getChoiceList(status, offset, pageSize);
        }
        model.addAttribute("count", count);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("list", list);
        model.addAttribute("status", status);
        return "/choice/list";
    }

    /**
     * 添加精选
     *
     * @param title
     * @param content
     * @param image
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public Result addChoice(@RequestParam(value = "title") String title,
                            @RequestParam(value = "content") String content,
                            @RequestParam(value = "publishTime",required = false)String publishTime,
                            @RequestParam(value = "image") String image) {
        boolean flag = choiceService.addChoice(title, content, image, publishTime);
        if(flag){
            return new Success("添加成功!");
        }else{
            return new Failed("添加失败!");
        }

    }

    /**
     * 删除精选
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "del")
    @ResponseBody
    public Result delChoice(@RequestParam(value = "id") Integer id){
        choiceService.delChoice(id);
        return  new Success("删除成功");
    }

    /**
     * 编辑
     * @param id            精选id
     * @param status        页面跳转参数
     * @param pageSize      页面跳转参数
     * @param pageNumber    页面跳转参数
     * @return
     */
    @RequestMapping(value = "edit")
    public String editChoice(@RequestParam(value = "id")Integer id,
                             @RequestParam(value = "status") Integer status,
                             @RequestParam(value = "pageSize") Integer pageSize,
                             @RequestParam(value = "pageNumber") Integer pageNumber,
                             Model model){
        Choice c = choiceService.getChoiceById(id);
        model.addAttribute("choice",c);
        model.addAttribute("status",status);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("pageNumber",pageNumber);
        return  "/choice/edit";


    }

    /**
     * 更新
     * @param id
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public Result update(@RequestParam(value = "id")      Integer id,
                         @RequestParam(value = "title")   String title,
                         @RequestParam(value = "content") String content,
                         @RequestParam(value = "image") String image,
                         @RequestParam(value = "publishTime") String publishTime){
        Choice choice = choiceService.getChoiceById(id);
       if(choice.getStatus() != 3) {
           List<String> tempUrl = choiceService.getImgUrl(content);
           List<String> realUrl;
           if (!CollectionUtils.isEmpty(tempUrl)) {
               // 下载图片
               realUrl = choiceService.downloadImg(tempUrl);
               if (realUrl.size() == tempUrl.size()) {
                   // 1.替换图片地址为服务器上图片地址
                   for (int i = 0; i < realUrl.size(); i++) {
                       content = content.replace(tempUrl.get(i), realUrl.get(i));
                   }
               } else {
                   return new Failed("富文本中图片下载不成功，更新失败");
               }
           }
           // 2.更新到数据库
           boolean flag = choiceService.updateChoice(id, title, content, image, publishTime);
          if(flag) {
              return new Success("更新成功!");
          }else{
              return new Failed("数据库插入,上传图片处理不成功，更新失败");
          }
       } else {
           return new Failed("更新失败,已发布精选，不允许编辑");
       }
    }


    /**
     * 上线下线
     * @param id
     * @param status
     * @return
     */

    @RequestMapping(value = "offlineOnline")
    @ResponseBody
    public Result offlineOnline(@RequestParam(value = "id") Integer id,
                                @RequestParam(value = "status")Integer status){
        String result;
        if (status == 4) {
            result = choiceService.publishChoiceNow(id);
        } else {
            result = choiceService.publishChoiceByTime(id, status);
        }
        if(null == result){
            return new Success();
        }else {
            return new Failed(result);
        }
    }

    @RequestMapping(value = "review")
    @ResponseBody
    public Result getChoiceContent(@RequestParam(value = "id") Integer id) {
        String content = choiceService.getChoiceById(id).getContent();
        return  new Success(content);
    }
}
