package com.qcl.shuidaxia.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qcl.utils.serializer.Date2StringSerializer;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Created by qcl on 2018/7/10.
 * 管理员
 */
@Entity
@Data
@DynamicUpdate//自动更新时间
@EntityListeners(AuditingEntityListener.class)
public class ShuiAdmin {

    //员工和管理员信息相关
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminId;//主键

    private String adminName;
    private String adminPassword;
    private String adminPhone;
    private String adminPhoneBeiYong;
    private String adminCardId;//身份证号
    private String adminFromTime;//入职时间
    private String adminOutTime;//离职时间
    private String beizhu;//备注

    private Integer adminType;//-1离职管理员，0普通会员不是管理员，1管理员，2超级管理员（可以管理管理员和员工）

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;//配送时间
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;


}
