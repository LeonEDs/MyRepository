package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xad.server.dto.TagParamRuleDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * 数据实体 - 标签参数规则.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tag_param_rules")
@NoArgsConstructor
public class TagParamRule extends BaseEntity
{
    /** 标签ID */
    private Long tagId;

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

    public TagParamRule(TagParamRuleDto dto)
    {
        super(dto);
        this.tagId = StringUtils.isEmpty(dto.getTagId()) ? null : Long.valueOf(dto.getTagId());
        this.tagParamId = dto.getTagParamId();
        this.indexId = dto.getIndexId();
        this.indexName = dto.getIndexName();
        this.indexType = dto.getIndexType();
        this.fieldId = dto.getFieldId();
        this.fieldName = dto.getFieldName();
        this.fieldType = dto.getFieldType();
        this.compareOperator = dto.getCompareOperator();
        this.compareValue = dto.getCompareValue();
        this.ruleOperator = dto.getRuleOperator();
        this.ruleStatus = dto.getRuleStatus();
    }
}
