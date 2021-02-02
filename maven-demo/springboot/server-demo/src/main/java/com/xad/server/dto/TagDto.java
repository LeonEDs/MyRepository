package com.xad.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.entity.TagEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 标签.
 *
 * @author xad
 * @version 1.0
 * @date 2020/12/24 0024
 */
@Data
@NoArgsConstructor
public class TagDto {

    /**
     * 主键ID.
     */
    private String id;

    /**
     * 创建人.
     */
    private String creator;

    /**
     * 修改人.
     */
    private String modifier;

    /**
     * 创建时间.
     */
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_PATTERN
            , timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date createTime;

    /**
     * 修改时间.
     */
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_PATTERN
            , timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date modifyTime;

    /**
     * 数据状态, 0正常 1删除.
     */
    private String status;

    /**
     * 标签参数信.
     */
    private List<TagParamDto> tagParamDtoList;

    /**
     * 标签目录全路径.
     */
    private String tagAllCatalogName;

    private String operateContent;

    /**
     * 标签编码.
     */
    private String tagCode;

    /**
     * 标签名称.
     */
    private String tagName;

    /**
     * 标签等级.
     */
    private String tagModule;

    /**
     * 标签等级对象.
     */
    private String tagModuleCode;

    /**
     * 标签等级对象名称.
     */
    private String tagModuleName;

    /**
     * 标签所属板块编码.
     */
    private String bkCode;

    /**
     * 标签目录ID.
     */
    private String tagCatalogId;

    /**
     * 标签生效时间.
     */
    @JsonFormat(pattern = DateTimeConstant.DATE_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date effectiveDate;

    /**
     * 标签失效时间.
     */
    @JsonFormat(pattern = DateTimeConstant.DATE_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date expiryDate;

    /**
     * 标签的业务场景描述.
     */
    private String tagDescription;

    /**
     * 标签属性.
     */
    private String tagAutomanual;

    /**
     * 标签计算类型.
     */
    private String calculationType;

    /**
     * 标签计算周期.
     */
    private String calculationCycles;

    /**
     * 标签值类型.
     */
    private String tagValueType;

    /**
     * 标签状态.
     */
    private String tagStatus;

    /**
     * 标签状态名称.
     */
    private String tagStatusName;

    /**
     * 版本号.
     */
    private Float version;

    /**
     * 拓展字段.
     */
    private String remark;

    public TagDto(TagEntity entity) {
        this.id = entity.getId() == null ? null : String.valueOf(entity.getId());
        this.creator = entity.getCreator();
        this.modifier = entity.getModifier();
        this.createTime = entity.getCreateTime();
        this.modifyTime = entity.getModifyTime();
        this.status = entity.getStatus();
        this.tagCode = entity.getTagCode();
        this.tagName = entity.getTagName();
        this.tagModule = entity.getTagModule();
        this.tagModuleCode = entity.getTagModuleCode();
        this.tagCatalogId = entity.getTagCatalogId() == null ? null : String.valueOf(entity.getTagCatalogId());
        this.effectiveDate = entity.getEffectiveDate();
        this.expiryDate = entity.getExpiryDate();
        this.tagDescription = entity.getTagDescription();
        this.tagAutomanual = entity.getTagAutomanual();
        this.calculationType = entity.getCalculationType();
        this.calculationCycles = entity.getCalculationCycles();
        this.tagValueType = entity.getTagValueType();
        this.tagStatus = entity.getTagStatus();
        this.version = entity.getVersion();
        this.remark = entity.getRemark();
    }
}
