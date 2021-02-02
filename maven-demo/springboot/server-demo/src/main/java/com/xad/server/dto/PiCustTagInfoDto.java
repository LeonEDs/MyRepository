package com.xad.server.dto;

import lombok.*;

import java.util.List;

/**
 * 客户标签 PI.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PiCustTagInfoDto {
    /**
     * 用户ID.
     */
    private Long custId;
    /**
     * 省份编码.
     */
    private String region;
    /**
     * 国家编码.
     */
    private String country;
    /**
     * 城市.
     */
    private String city;
    /**
     * 城市编码.
     */
    private String cityCode;
    /**
     * 区县编码.
     */
    private String countyCode;
    /**
     * 街道编码.
     */
    private String streetCode;
    /**
     * 详细地址.
     */
    private String detailAddress;
    /**
     * 状态.
     */
    private String status;
    /**
     * 客户编码.
     */
    private String customerCode;
    /**
     * 证件类型.
     */
    private String certType;
    /**
     * 证件号码.
     */
    private String certNum;
    /**
     * 工商登记证号.
     */
    private String bcerCode;
    /**
     * 税务登记证号.
     */
    private String taxCode;
    /**
     * 组织机构代码.
     */
    private String orgCode;
    /**
     * 统一社会信用代码.
     */
    private String soialCrCode;

    /**
     * 标签信息.
     */
    private List<PiTagInfoDto> custTag;

}
