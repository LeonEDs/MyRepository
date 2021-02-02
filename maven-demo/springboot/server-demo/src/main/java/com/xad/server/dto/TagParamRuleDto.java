package com.xad.server.dto;

import com.xad.server.entity.TagParamRule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 标签参数规则.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TagParamRuleDto extends BaseDto
{
    /** 标签ID */
    private String tagId;

    /** 标签参数ID */
    private Long tagParamId;

    /** 指标ID */
    private Long indexId;

    /** 指标名称 */
    private String indexName;

    /** 指标类型 */
    private String indexType;

    /** 指标字段Id */
    private Long fieldId;

    /** 指标字段名称 */
    private String fieldName;

    /** 指标字段类型 */
    private String fieldType;

    /** 计算符 */
    private String compareOperator;

    /** 计算比较值 */
    private String compareValue;

    /** 规则关系符 */
    private String ruleOperator;

    /** 标签参数规则状态 */
    private String ruleStatus;

    public TagParamRuleDto(TagParamRule entity)
    {
        super(entity);
        this.tagId = entity.getTagId() == null ? null : String.valueOf(entity.getTagId());
        this.tagParamId = entity.getTagParamId();
        this.indexId = entity.getIndexId();
        this.indexName = entity.getIndexName();
        this.indexType = entity.getIndexType();
        this.fieldId = entity.getFieldId();
        this.fieldName = entity.getFieldName();
        this.fieldType = entity.getFieldType();
        this.compareOperator = entity.getCompareOperator();
        this.compareValue = entity.getCompareValue();
        this.ruleOperator = entity.getRuleOperator();
        this.ruleStatus = entity.getRuleStatus();
    }
}
