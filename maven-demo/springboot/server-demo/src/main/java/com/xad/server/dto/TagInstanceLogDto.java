package com.xad.server.dto;

import com.xad.server.entity.TagInstance;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 标签实例.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TagInstanceLogDto extends BaseDto
{
    /** 标签ID */
    private String tagId;

    /** 标签编码 */
    private String tagCode;

    /** 标签名称 */
    private String tagName;

    /** 标签名称 */
    private String tagDescribe;

    /** 客户ID */
    private Long custId;

    /** 标签值 */
    private String tagValue;

    /** 任务编号 */
    private String executionCode;

    /** 拓展字段 */
    private String remark;

    private String type;

    public TagInstanceLogDto(TagInstance entity)
    {
        super(entity);
        this.tagId = entity.getTagId() == null ? null : String.valueOf(entity.getTagId());
        this.tagCode = entity.getTagCode();
        this.custId = entity.getCustId();
        this.tagValue = entity.getTagValue();
        this.executionCode = entity.getExecutionCode();
        this.remark = entity.getRemark();
    }
}
