package com.oupeng.joke.back.service;

import com.oupeng.joke.dao.mapper.FeedbackMapper;
import com.oupeng.joke.domain.LineData;
import com.oupeng.joke.domain.LineDataSource;
import com.oupeng.joke.domain.LineGraph;
import com.oupeng.joke.domain.PieGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 内容源服务
 * Created by hushuang on 16/7/6.
 */
@Service
public class FeedbackService {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);
    @Autowired
    private FeedbackMapper feedbackMapper;
    private static final String REFRESH = "刷新慢";
    private static final String NO_FUNNY = "不好笑";
    private static final String QUIT = "闪退/黑屏";
    private static final String OTHER = "其他";
    private static String[] colors = {"#fedd74", "#82d8ef", "#f76864", "#80bd91", "#fd9fc1", "#a5c2d5", "#cbab4f", "#76a871", "#a56f8f"};
    /**
     * 查询渠道反馈数量列表
     * @return
     * @param startDate
     * @param endDate
     * @param distributorId
     */
    public List<PieGraph> getDistributorFeedbackCountList(String startDate, String endDate, Integer distributorId) {
        return feedbackMapper.getDistributorFeedbackCountList(startDate, endDate, distributorId);
    }

    /**
     * 获取渠道反馈类型列表
     * @return
     * @param startDate
     * @param endDate
     * @param distributorId
     */
    public LineGraph getDistributorFeedbackTypeList(String startDate, String endDate, Integer distributorId) {
        List<LineDataSource> list = feedbackMapper.getDistributorFeedbackTypeList(startDate, endDate, distributorId);
        List<String> labels = new ArrayList<>();
        int size = list.size();
        List<Integer> refresh = new ArrayList<>();
        List<Integer> noFunny = new ArrayList<>();
        List<Integer> quit = new ArrayList<>();
        List<Integer> other = new ArrayList<>();
        int max = 5;
        for(LineDataSource f : list){
            labels.add(f.getCreateTime());
            refresh.add(f.getRefresh());
            noFunny.add(f.getNoFunny());
            quit.add(f.getQuit());
            other.add(f.getOther());
            if(f.getRefresh() > max){
                max = f.getRefresh();
            }
            if(f.getNoFunny() > max){
                max = f.getNoFunny();
            }
            if(f.getQuit() > max){
                max = f.getQuit();
            }
            if(f.getOther() > max){
                max = f.getOther();
            }
        }
        max += 3;
        List<LineData> lineDatas = new ArrayList<>();
        LineData refreshData = new LineData();
        refreshData.setName(REFRESH);
        refreshData.setValue(refresh.toArray(new Integer[size]));
        refreshData.setColor(colors[0]);
        lineDatas.add(refreshData);
        LineData noFunnyData = new LineData();
        noFunnyData.setName(NO_FUNNY);
        noFunnyData.setValue(noFunny.toArray(new Integer[size]));
        noFunnyData.setColor(colors[1]);
        lineDatas.add(noFunnyData);
        LineData quitData = new LineData();
        quitData.setName(QUIT);
        quitData.setValue(quit.toArray(new Integer[size]));
        quitData.setColor(colors[2]);
        lineDatas.add(quitData);
        LineData otherData = new LineData();
        otherData.setName(OTHER);
        otherData.setValue(other.toArray(new Integer[size]));
        otherData.setColor(colors[3]);
        lineDatas.add(otherData);
        LineGraph lg = new LineGraph();
        lg.setData(lineDatas);
        lg.setLabels(labels.toArray(new String[size]));
        lg.setMax(max);
        lg.setSpace(getSpace(max));
        logger.info("--------linegraph:" , lg);
        return lg;
    }

    private Integer getSpace(int max) {
        if(max < 10){
            return 2;
        } else if(max < 50){
            return 5;
        } else if(max < 100){
            return 10;
        } else if(max < 500){
            return 20;
        } else if(max < 5000){
            return 100;
        } else if(max < 50000){
            return 1000;
        } else if(max < 500000){
            return 10000;
        }else {
            return 100000;
        }
    }
}
