package com.xad.server.service;

import com.xad.server.dto.TagInstanceDto;
import com.xad.server.entity.TagInstance;

import java.util.List;

/**
 * 标签实例 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface TagInstanceService
{
    /**
     * 获取客户Id的最大值
     */
    Long getMaxCustId();

    /**
     * 获取客户Id的最小值
     */
    Long getMinCustId();

    /**
     * 标签实例 根据客户ID查询
     * @param custId 客户ID
     */
    List<TagInstanceDto> queryTagInstanceByCustId(Long custId);

    /**
     * 标签实例 根据标签编码批量删除
     * @param tagCode 标签编码
     */
    int deleteBatchByTagCode(String tagCode);

    /**
     * 标签实例 根据标签编码和客户ID删除记录
     * @param tagCode 标签编码
     * @param custId 客户ID
     */
    int deleteByTagCodeWithCustId(String tagCode, Long custId);

    int deleteByTagCodeWithCustId(String tagCode, Long startCustId, Long endCustId);

    /**
     * 标签实例 批量新增
     * @param tagInstanceList 标签实例
     */
    void insertBatch(List<TagInstance> tagInstanceList);

    /**
     * 批量修改标签实例
     * @param tagInstance 修改
     * @param tagCode 查询条件
     */
    int updateBatchByTagCode(TagInstance tagInstance, String tagCode);

    /**
     * 删除客户标签
     */
    String removeTagInstance(Long id);
}
