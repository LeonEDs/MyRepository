package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 基础 实体类.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@NoArgsConstructor
public class BaseEntity {
    /**
     * 主键ID.
     */
    @TableId(value = "id",type = IdType.AUTO)
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

    public BaseEntity(BaseDto dto)
    {
        this.id = dto.getId();
        this.creator = dto.getCreator();
        this.modifier = dto.getModifier();
        this.createTime = dto.getCreateTime();
        this.modifyTime = dto.getModifyTime();
        this.status = dto.getStatus();
    }
}
