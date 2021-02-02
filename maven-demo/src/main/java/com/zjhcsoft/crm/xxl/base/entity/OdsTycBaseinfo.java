package com.zjhcsoft.crm.xxl.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 天眼查企业基本信息
 * </p>
 *
 * @author ${author}
 * @since 2021-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ods_tyc_baseinfo")
public class OdsTycBaseinfo extends Model<OdsTycBaseinfo> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 曾用名
     */
    private String historyNames;

    /**
     * 企业状态
     */
    private String regStatus;

    /**
     * 注销日期
     */
    private LocalDateTime cancelDate;

    /**
     * 注册资本
     */
    private String regCapital;

    /**
     * 成员规模
     */
    private String staffNumRange;

    /**
     * 行业
     */
    private String industry;

    /**
     * 股票号
     */
    private String bondNum;

    /**
     * 法人类型:1 人, 2 公司
     */
    private Integer type;

    /**
     * 股票名
     */
    private String bondName;

    /**
     * 吊销日期
     */
    private LocalDateTime revokeDate;

    /**
     * 法人
     */
    private String legalPersonName;

    /**
     * 注册号
     */
    private String revokeReason;

    /**
     * 英文名
     */
    private String property3;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 股票曾用名
     */
    private String usedBondName;

    /**
     * 经营开始时间
     */
    private LocalDateTime fromTime;

    /**
     * 核准时间
     */
    private LocalDateTime approvedTime;

    /**
     * 参保人数
     */
    private Integer socialStaffNum;

    /**
     * 简称
     */
    private String alias;

    /**
     * 企业类型
     */
    private String companyOrgType;

    /**
     * 实收注册资本币种 人民币 美元 欧元 等（暂未使用）
     */
    private String actualCapitalCurrency;

    /**
     * 企业id
     */
    private Long entId;

    /**
     * 组织机构代码
     */
    private String orgNumber;

    /**
     * 注销原因
     */
    private String cancelReason;

    /**
     * 经营结束时间
     */
    private LocalDateTime toTime;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 实收注册资本
     */
    private String actualCapital;

    /**
     * 成立日期
     */
    private LocalDateTime estiblishTime;

    /**
     * 登记机关
     */
    private String regInstitute;

    /**
     * 纳税人识别号
     */
    private String taxNumber;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 注册地址
     */
    private String regLocation;

    /**
     * 注册资本币种 人民币 美元 欧元 等（暂未使用）
     */
    private String regCapitalCurrency;

    /**
     * 企业标签
     */
    private String tags;

    /**
     * 网址
     */
    private String websiteList;

    /**
     * 企业联系方式
     */
    private String phoneNumber;

    /**
     * 企业名
     */
    private String name;

    /**
     * 股票类型
     */
    private String bondType;

    /**
     * 企业评分
     */
    private Integer percentileScore;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createDt;

    /**
     * 修改时间
     */
    private LocalDateTime lastUpDt;

    /**
     * 企业ent_customer客户ID
     */
    private Integer custId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
