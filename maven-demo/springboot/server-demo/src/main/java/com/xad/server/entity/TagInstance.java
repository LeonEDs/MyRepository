package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xad.server.dto.TagInstanceDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

/**
 * 数据实体 - 标签实例.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "stc_gg_sale_taginst")
@NoArgsConstructor
@ToString
public class TagInstance extends BaseEntity
{
    /** 标签ID */
    private Long tagId;

    /** 标签名称 */
    @TableField(exist = false)
    private String tagName;

    @TableField(exist = false)
    private String tagAutoType;

    @TableField(exist = false)
    private String tagDescribe;

    /** 标签编码 */
    private String tagCode;

    /** 客户ID */
    private Long custId;

    /** 标签值 */
    private String tagValue;

    /** 任务编号 */
    private String executionCode;

    /** 拓展字段 */
    private String remark;

    public TagInstance(TagInstanceDto dto)
    {
        super(dto);
        this.tagId = StringUtils.isEmpty(dto.getTagId()) ? null : Long.valueOf(dto.getTagId());
        this.tagCode = dto.getTagCode();
        this.custId = dto.getCustId();
        this.tagValue = dto.getTagValue();
        this.executionCode = dto.getExecutionCode();
        this.remark = dto.getRemark();
    }
}
