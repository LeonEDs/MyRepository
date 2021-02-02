package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xad.server.dto.OrganizationDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据实体 - 组织信息.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@TableName(value = "organization")
@NoArgsConstructor
public class Organization
{
    private String comCode;
    private String vendorCode;
    private String customerCode;
    private String abbrName;
    private String fullName;
    /**
     * 板块编码.
     */
    private String bktype;
    private String hoidingJt;
    /**
     * 是否是营运企业（否为营运企业）.
     */
    private String isbk;
    private String isbranch;
    private String lastComM;
    private String hoidingLast;
    private String levelM;
    private String lastComP;
    private String levelP;
    private String invalid;
    private String beyong1;
    private String beyong2;
    private String beyong3;
    private String beyong4;
    private String beyong5;
    private String beyong6;
    private String mdmUpdateDate;
    private String mdmCreateDate;

    public Organization(OrganizationDto dto)
    {
        this.comCode = dto.getComCode();
        this.vendorCode = dto.getVendorCode();
        this.customerCode = dto.getCustomerCode();
        this.abbrName = dto.getAbbrName();
        this.fullName = dto.getFullName();
        this.bktype = dto.getBktype();
        this.hoidingJt = dto.getHoidingJt();
        this.isbk = dto.getIsbk();
        this.isbranch = dto.getIsbranch();
        this.lastComM = dto.getLastComM();
        this.hoidingLast = dto.getHoidingLast();
        this.levelM = dto.getLevelM();
        this.lastComP = dto.getLastComP();
        this.levelP = dto.getLevelP();
        this.invalid = dto.getInvalid();
        this.beyong1 = dto.getBeyong1();
        this.beyong2 = dto.getBeyong2();
        this.beyong3 = dto.getBeyong3();
        this.beyong4 = dto.getBeyong4();
        this.beyong5 = dto.getBeyong5();
        this.beyong6 = dto.getBeyong6();
        this.mdmUpdateDate = dto.getMdmUpdateDate();
        this.mdmCreateDate = dto.getMdmCreateDate();
    }
}
