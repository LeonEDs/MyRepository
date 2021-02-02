package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xad.server.dto.TagParamDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * 数据实体 - 标签参数.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tag_params")
@NoArgsConstructor
public class TagParam extends BaseEntity
{
    /** 标签ID */
    private Long tagId;

    /** 标签参数值 */
    private String paramValue;

    /** 标签参数状态 */
    private String paramStatus;

    /** 描述 */
    private String description;

    public TagParam(TagParamDto dto)
    {
        super(dto);
        this.tagId = StringUtils.isEmpty(dto.getTagId()) ? null : Long.valueOf(dto.getTagId());
        this.paramValue = dto.getParamValue();
        this.paramStatus = dto.getParamStatus();
        this.description = dto.getDescription();
    }
}
