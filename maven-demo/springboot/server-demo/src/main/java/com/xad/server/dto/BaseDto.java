package com.xad.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 基础 拓展类.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@NoArgsConstructor
public class BaseDto
{
    /**
     * 主键ID.
     */
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

    public BaseDto(BaseEntity entity)
    {
        this.id = entity.getId();
        this.creator = entity.getCreator();
        this.modifier = entity.getModifier();
        this.createTime = entity.getCreateTime();
        this.modifyTime = entity.getModifyTime();
        this.status = entity.getStatus();
    }
}
