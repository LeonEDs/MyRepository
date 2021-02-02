package com.xad.server.dto;

import com.xad.server.entity.TagOperationLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 标签和标签策略操作日志.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TagOperationLogDto extends BaseDto
{
    /** 操作对象Code */
    private String objCode;

    /** 操作对象类型 */
    private String objType;

    /** 操作类型 */
    private String operationType;

    /** 操作类型 */
    private String operationName;

    /** 版本号 */
    private Float version;

    /** 说明 */
    private String description;

    public TagOperationLogDto(TagOperationLog entity)
    {
        super(entity);
        this.objCode = entity.getObjCode();
        this.objType = entity.getObjType();
        this.operationType = entity.getOperationType();
        this.version = entity.getVersion();
        this.description = entity.getDescription();
    }
}
