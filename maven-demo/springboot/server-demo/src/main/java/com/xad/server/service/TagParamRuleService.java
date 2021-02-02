package com.xad.server.service;

import com.xad.server.dto.TagParamRuleDto;
import com.xad.server.entity.TagParamRule;

import java.util.List;

/**
 * 标签参数规则 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface TagParamRuleService
{
    /**
     * 标签参数规则 查询
     * @param vo 查询条件
     * @return 标签参数规则
     */
    List<TagParamRuleDto> queryTagParamRuleList(TagParamRuleDto vo);

    /**
     * 标签参数规则 查询
     * @param tagId 查询条件
     * @return 标签参数规则
     */
    List<TagParamRule> queryTagParamRuleByTagId(Long tagId);

    /**
     * 标签参数规则 查询
     * @param tagParamId 查询条件
     * @return 标签参数规则
     */
    List<TagParamRule> queryTagParamRuleByTagParamId(Long tagParamId);

    /**
     * 标签参数规则 重建记录
     */
    void rebuildBatch(List<TagParamRule> tagParamRuleList, Long tagId, Long paramId);

    /**
     * 标签参数规则 批量修改
     */
    void updateBatch(List<TagParamRule> tagParamRuleList);

    /**
     * 标签参数规则 批量删除
     */
    void deleteBatch(List<TagParamRule> tagParamRuleList);

    /**
     * 标签参数规则 批量新增
     */
    void insertBatch(List<TagParamRule> tagParamRuleList);

    /**
     * 标签参数规则 批量过期数据
     */
    void deprecatedBatch(List<Long> tagParamRuleIdList);
}
