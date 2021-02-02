package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.dto.TagDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 数据实体 - 标签.
 *
 * @author xad
 * @version 1.0
 * @date 2020/12/24 0024
 */
@Data
@TableName(value = "t_tags")
@NoArgsConstructor
public class TagEntity {
    /**
     * 主键ID.
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

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
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date createTime;

    /**
     * 修改时间.
     */
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date modifyTime;

    /**
     * 数据状态, 0正常 1删除.
     */
    private String status;

    /**
     * 标签编码.
     */
    private String tagCode;

    /**
     * 标签名称.
     */
    private String tagName;

    /**
     * 标签等级
     */
    private String tagModule;

    /**
     * 标签等级对象
     */
    private String tagModuleCode;

    /**
     * 标签目录ID
     */
    private Long tagCatalogId;

    /**
     * 标签生效时间
     */
    @JsonFormat(pattern = DateTimeConstant.DATE_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date effectiveDate;

    /**
     * 标签失效时间
     */
    @JsonFormat(pattern = DateTimeConstant.DATE_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date expiryDate;

    /**
     * 标签的业务场景描述
     */
    private String tagDescription;

    /**
     * 标签属性
     */
    private String tagAutomanual;

    /**
     * 标签计算类型
     */
    private String calculationType;

    /**
     * 标签计算周期
     */
    private String calculationCycles;

    /**
     * 标签值类型
     */
    private String tagValueType;

    /**
     * 标签状态
     */
    private String tagStatus;

    /**
     * 版本号
     */
    private Float version;

    /**
     * 拓展字段
     */
    private String remark;

    public TagEntity(TagDto dto) {
        this.id = StringUtils.isEmpty(dto.getId()) ? null : Long.valueOf(dto.getId());
        this.creator = dto.getCreator();
        this.modifier = dto.getModifier();
        this.createTime = dto.getCreateTime();
        this.modifyTime = dto.getModifyTime();
        this.status = dto.getStatus();
        this.tagCode = dto.getTagCode();
        this.tagName = dto.getTagName();
        this.tagModule = dto.getTagModule();
        this.tagModuleCode = dto.getTagModuleCode();
        this.tagCatalogId = StringUtils.isEmpty(dto.getTagCatalogId()) ? null : Long.valueOf(dto.getTagCatalogId());
        this.effectiveDate = dto.getEffectiveDate();
        this.expiryDate = dto.getExpiryDate();
        this.tagDescription = dto.getTagDescription();
        this.tagAutomanual = dto.getTagAutomanual();
        this.calculationType = dto.getCalculationType();
        this.calculationCycles = dto.getCalculationCycles();
        this.tagValueType = dto.getTagValueType();
        this.tagStatus = dto.getTagStatus();
        this.version = dto.getVersion();
        this.remark = dto.getRemark();
    }
}
