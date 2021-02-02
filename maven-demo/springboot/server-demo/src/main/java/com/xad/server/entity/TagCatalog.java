package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.dto.TagCatalogDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * 数据实体 - 标签目录.
 *
 * @author xad
 * @version 1.0
 * @date 2020/12/24 0024
 */
@Data
@TableName(value = "tag_catalogs")
@NoArgsConstructor
public class TagCatalog {
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
     * 标签目录名称.
     */
    private String catalogName;
    /**
     *
     */
    private String catalogCode;

    /**
     * 标签目录描述.
     */
    private String description;

    /**
     * 上级.
     */
    private Long parentId;

    /**
     * 层级.
     */
    private Integer level;

    /**
     * 排序.
     */
    private Integer sequence;

    public TagCatalog(TagCatalogDto dto) {
        this.id = StringUtils.isEmpty(dto.getId()) ? null : Long.valueOf(dto.getId());
        this.creator = dto.getCreator();
        this.modifier = dto.getModifier();
        this.createTime = dto.getCreateTime();
        this.modifyTime = dto.getModifyTime();
        this.status = dto.getStatus();
        this.catalogName = dto.getCatalogName();
        this.description = dto.getDescription();
        this.parentId = StringUtils.isEmpty(dto.getParentId()) ? null : Long.valueOf(dto.getParentId());
        this.level = dto.getLevel();
        this.sequence = dto.getSequence();
    }
}
