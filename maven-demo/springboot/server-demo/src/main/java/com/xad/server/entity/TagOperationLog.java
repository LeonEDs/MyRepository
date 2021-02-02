package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xad.server.dto.TagOperationLogDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 数据实体 - 标签和标签策略操作日志.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tag_operation_logs")
@NoArgsConstructor
public class TagOperationLog extends BaseEntity
{
    /** 操作对象Code. */
    private String objCode;

    /** 操作对象类. */
    private String objType;

    /** 操作类. */
    private String operationType;

    /** 版本. */
    private Float version;

    /** 说. */
    private String description;

    public TagOperationLog(TagOperationLogDto dto)
    {
        super(dto);
        this.objCode = dto.getObjCode();
        this.objType = dto.getObjType();
        this.operationType = dto.getOperationType();
        this.version = dto.getVersion();
        this.description = dto.getDescription();
    }
}
