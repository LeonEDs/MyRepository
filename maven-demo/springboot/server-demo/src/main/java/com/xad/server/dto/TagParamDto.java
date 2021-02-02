package com.xad.server.dto;

import com.xad.server.entity.TagParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 标签参数.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TagParamDto extends BaseDto
{
    private List<TagParamRuleDto> tagParamRuleDtoList;

    /** 标签ID */
    private String tagId;

    /** 标签参数值 */
    private String paramValue;

    /** 标签参数状态 */
    private String paramStatus;

    /** 描述 */
    private String description;

    public TagParamDto(TagParam entity)
    {
        super(entity);
        this.tagId = entity.getTagId() == null ? null : String.valueOf(entity.getTagId());
        this.paramValue = entity.getParamValue();
        this.paramStatus = entity.getParamStatus();
        this.description = entity.getDescription();
    }
}
