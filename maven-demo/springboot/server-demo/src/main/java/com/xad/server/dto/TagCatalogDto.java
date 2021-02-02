package com.xad.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.entity.TagCatalog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 标签目录.
 *
 * @author xad
 * @version 1.0
 * @date 2020/12/24 0024
 */
@Data
@NoArgsConstructor
public class TagCatalogDto{
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

    private List<TagCatalogDto> childList;

    private Boolean isLeaf = true;

    /**
     * 标签目录名称.
     */
    private String catalogName;

    /**
     * 标签目录描述.
     */
    private String description;

    /**
     * 上级.
     */
    private String parentId;

    /**
     * 层级.
     */
    private Integer level;

    /**
     * 排序.
     */
    private Integer sequence;

    public TagCatalogDto(TagCatalog entity) {
        this.id = entity.getId() == null ? null : String.valueOf(entity.getId());
        this.creator = entity.getCreator();
        this.modifier = entity.getModifier();
        this.createTime = entity.getCreateTime();
        this.modifyTime = entity.getModifyTime();
        this.status = entity.getStatus();
        this.catalogName = entity.getCatalogName();
        this.description = entity.getDescription();
        this.parentId = entity.getParentId() == null ? null : String.valueOf(entity.getParentId());
        this.level = entity.getLevel();
        this.sequence = entity.getSequence();
    }
}
