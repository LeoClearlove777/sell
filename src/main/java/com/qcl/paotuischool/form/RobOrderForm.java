package com.qcl.paotuischool.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Created by qcl on 2018/3/18.
 * 用来验证用户注册提交的表单字段
 */
@Data
public class RobOrderForm {
    @NotEmpty(message = "订单号必传")
    private String orderid;
    @NotEmpty(message = "微信id必传")
    private String openid;
    @NotEmpty(message = "跑腿员姓名不能为空")
    private String name;
    @Pattern(regexp = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$", message = "手机号码格式错误")
    @NotEmpty(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号必须是11位")
    private String phone;
}
