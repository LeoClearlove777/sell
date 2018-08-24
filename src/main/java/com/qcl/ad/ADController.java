package com.qcl.ad;

import com.qcl.api.ResultApi;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 跑腿
 */

@RestController
@RequestMapping("/guanggao")
@Slf4j
public class ADController {

    @Autowired
    private ADService service;


    //收入列表
    @GetMapping("/shouruList")
    public ResultApi shouruList() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        List<IncomeBean> beans = service.findAll(sort);
        return ResultApiUtil.success(beans);
    }

    //获取某一天的广告点击排名
    @PostMapping("/clikcList")
    public ResultApi clikcList(@RequestParam(name = "dateTime") String dateTime) {
        Sort sort = new Sort(Sort.Direction.DESC, "clickNum");
        List<AdClickBean> beans = service.findClickList(dateTime, sort);
        return ResultApiUtil.success(beans);
    }

    //某一天广告点击增加
    @PostMapping("/clikcAdd")
    public ResultApi clikcAdd(@RequestParam(name = "dateTime") String dateTime,
                              @RequestParam(name = "openid") String openid,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "city") String city) {
        List<AdClickBean> existList = service.findHasExist(dateTime, name, openid);

        AdClickBean newBean = new AdClickBean();
        if (existList != null && existList.size() > 0) {//今天已经点击过
            newBean = existList.get(0);//如果存在多个用户，只取第一个
            newBean.setClickNum(newBean.getClickNum() + 1);
        } else {
            newBean.setOpenid(openid);
            newBean.setName(name);
            newBean.setCity(city);
            newBean.setClickNum(1);
            newBean.setDateTime(dateTime);
        }
        return ResultApiUtil.success(service.save(newBean));
    }

    //某一周广告点击增加
    @PostMapping("/clickWeekAdd")
    public ResultApi clickWeekAdd(
            @RequestParam(name = "weekTime") String weekTime,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "isVideo") boolean isVideo,
            @RequestParam(name = "city") String city) {
        List<AdClickWeekBean> existList = service.findWeekHasExist(weekTime, name);

        AdClickWeekBean newBean = new AdClickWeekBean();
        if (existList != null && existList.size() > 0) {//今天已经点击过
            newBean = existList.get(0);//如果存在多个用户，只取第一个
            if (isVideo) {
                newBean.setClickVideoNum(newBean.getClickVideoNum() + 1);
                newBean.setClickAdNum(newBean.getClickAdNum() + 15);
            } else {
                newBean.setClickAdNum(newBean.getClickAdNum() + 1);
            }
        } else {
            newBean.setWeekTime(weekTime);
            newBean.setName(name);
            newBean.setCity(city);
            newBean.setSalary("待分配");
            if (isVideo) {
                newBean.setClickVideoNum(1);
                newBean.setClickAdNum(15);
            } else {
                newBean.setClickVideoNum(0);
                newBean.setClickAdNum(0);
            }

        }
        return ResultApiUtil.success(service.saveWeek(newBean));
    }


}