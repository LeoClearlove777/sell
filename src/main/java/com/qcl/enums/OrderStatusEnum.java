package com.qcl.enums;

import lombok.Getter;

/**
 * Created by qcl on 2018/3/13.
 * 订单状态
 */
@Getter
public enum OrderStatusEnum {
    NEW(0, "新下订单"),
    FINISHED(1, "完成订单"),
    CANCEL(2, "取消订单");

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

}