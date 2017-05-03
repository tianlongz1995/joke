package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.SensitiveFilterService;
import com.oupeng.joke.domain.Sensitive;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by java_zong on 2017/4/28.
 */
@Controller
@RequestMapping(value = "/sensitive")
public class SensitiveController {
    @Autowired
    private SensitiveFilterService sensitiveService;

    /**
     * 敏感词列表
     *
     * @param keyWord
     * @param pageNumber
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "keyWord", required = false) String keyWord,
                       @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                       Model model) {

        pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
        pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
        int pageCount = 0;//总页数
        int offset = 0;//开始条数index
        List<Sensitive> list = null;
        //	获取总条数
        int count = sensitiveService.getListForCount(keyWord);
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
            list = sensitiveService.getList(keyWord, offset, pageSize);
        }
        model.addAttribute("list", list);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("count", count);
        return "/sensitive/list";
    }

    /**
     * 添加
     *
     * @param word
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestParam(value = "word") String word) {
        if (sensitiveService.add(word)) {
            return new Success();
        } else {
            return new Failed("该敏感词已存在");
        }
    }

    /**
     * 删除敏感词
     *
     * @param id
     * @return
     */
    @RequestMapping("/del")
    @ResponseBody
    public Result deleteWord(@RequestParam Integer id) {
        if (sensitiveService.deleteWord(id)) {
            return new Success();
        } else {
            return new Failed("删除失败");
        }
    }
}
