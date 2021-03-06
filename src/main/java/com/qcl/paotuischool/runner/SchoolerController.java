package com.qcl.paotuischool.runner;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.paotuischool.bean.SchoolRunner;
import com.qcl.paotuischool.form.RunnerForm;
import com.qcl.utils.ResultApiUtil;
import com.qcl.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 跑腿
 */

@RestController
@RequestMapping("/schooler")
@Slf4j
public class SchoolerController {

    @Autowired
    private SchoolerService service;


    //根据openid获取跑腿员的用户信息
    @GetMapping("/openid")
    public ResultApi findOne(@RequestParam(name = "openid") String openid) {
        SchoolRunner bean = service.findOneOpenid(openid);
        return ResultApiUtil.success(bean);
    }


    /**
     * 用户注册或重置信息
     * 使用BindingResult校验参数
     *
     * @param runnerForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public ResultApi save(@Valid RunnerForm runnerForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[注册用户] 参数不正确，runnerForm={}", runnerForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }


        SchoolRunner bean = new SchoolRunner();
        bean.setOpenId(runnerForm.getOpenid());
        bean.setName(runnerForm.getName());
        bean.setPhone(runnerForm.getPhone());
        bean.setCardId(runnerForm.getCardid());
        bean.setAddress(runnerForm.getAddress());
        bean.setType(1);//默认新注册用户都处于审核中的状态

        SchoolRunner bean1 = service.save(bean);
        return ResultApiUtil.success(bean1);
    }


    /**
     * 保存formid，将来做小程序推送用
     */
    @PostMapping("/formid")
    public ResultApi formid(
            @RequestParam("openid") String openid,
            @RequestParam("formid") String formid) {
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(formid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        SchoolRunner runner = service.findOneOpenid(openid);
        if (runner.getType() < 2) {
            log.error("[推送] 只收集跑腿员的formid");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        String formIds = runner.getFormIds();
        List list = new ArrayList<>();
        if (StringUtils.isEmpty(formIds)) {
            list.add(formid);
        } else {
            list = Utils.String2List(formIds);
            list.add(formid);
        }
        runner.setFormIds(Utils.List2String(list));

        SchoolRunner bean1 = service.save(runner);
        return ResultApiUtil.success(bean1);
    }

    /*
    * 获取跑腿员列表
    * runnerType：1待审核，其余都是已经审核过
    * */
    @PostMapping("/getRunnerList")
    public ResultApi getRunnerList(
            @RequestParam("openid") String openid,
            @RequestParam("runnerType") int runnerType) {
        if (StringUtils.isEmpty(openid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        SchoolRunner runner = service.findOneOpenid(openid);
        if (runner.getAdminType() != 66) {
            log.error("[管理员获取信息] 不是管理员");
            throw new SellException(ResultEnum.USER_ADMIN_NO);
        }
        List<SchoolRunner> allRunner = service.findAllRunner(runnerType);
        return ResultApiUtil.success(allRunner);
    }

    /*
    * 获取跑腿员列表
    * runnerType：1待审核，其余都是已经审核过
    * */
    @PostMapping("/changeRunnerType")
    public ResultApi changeRunnerType(
            @RequestParam("adminOpenid") String adminOpenid,
            @RequestParam("runnerOpenid") String runnerOpenid,
            @RequestParam("runnerType") int runnerType) {
        if (StringUtils.isEmpty(adminOpenid) || StringUtils.isEmpty(runnerOpenid)) {
            log.error("[查询订单列表] openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        SchoolRunner runner = service.findOneOpenid(adminOpenid);
        if (runner.getAdminType() != 66) {
            log.error("[管理员获取信息] 不是管理员");
            throw new SellException(ResultEnum.USER_ADMIN_NO);
        }

        SchoolRunner schoolRunner = service.findOneOpenid(runnerOpenid);
        if (schoolRunner == null) {
            log.error("[管理员获取信息] 不是管理员");
            throw new SellException(ResultEnum.USER_NO_HAVE);
        }
        if (runnerType == -1) {//拒绝
            schoolRunner.setType(-1);
            schoolRunner.setRefuseDesc("您已被拒绝，请联系管理");
        } else if (runnerType == 2) {
            schoolRunner.setType(2);
            schoolRunner.setRefuseDesc("可以代取快递");
        } else if (runnerType == 3) {
            schoolRunner.setType(3);
            schoolRunner.setRefuseDesc("可以代取代寄快递");
        }
        SchoolRunner bean1 = service.save(schoolRunner);
        return ResultApiUtil.success(bean1);
    }

}
