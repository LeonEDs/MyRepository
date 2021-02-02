package com.xad.server.dto;

import com.xad.server.entity.Organization;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组织信息.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@NoArgsConstructor
public class OrganizationDto
{
    private String comCode;
    private String vendorCode;
    private String customerCode;
    private String abbrName;
    private String fullName;
    /** 板块编码. */
    private String bktype;
    private String hoidingJt;
    /** 是否是营运企业（否为营运企业）. */
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

    public OrganizationDto(Organization entity)
    {
        this.comCode = entity.getComCode();
        this.vendorCode = entity.getVendorCode();
        this.customerCode = entity.getCustomerCode();
        this.abbrName = entity.getAbbrName();
        this.fullName = entity.getFullName();
        this.bktype = entity.getBktype();
        this.hoidingJt = entity.getHoidingJt();
        this.isbk = entity.getIsbk();
        this.isbranch = entity.getIsbranch();
        this.lastComM = entity.getLastComM();
        this.hoidingLast = entity.getHoidingLast();
        this.levelM = entity.getLevelM();
        this.lastComP = entity.getLastComP();
        this.levelP = entity.getLevelP();
        this.invalid = entity.getInvalid();
        this.beyong1 = entity.getBeyong1();
        this.beyong2 = entity.getBeyong2();
        this.beyong3 = entity.getBeyong3();
        this.beyong4 = entity.getBeyong4();
        this.beyong5 = entity.getBeyong5();
        this.beyong6 = entity.getBeyong6();
        this.mdmUpdateDate = entity.getMdmUpdateDate();
        this.mdmCreateDate = entity.getMdmCreateDate();
    }
}
