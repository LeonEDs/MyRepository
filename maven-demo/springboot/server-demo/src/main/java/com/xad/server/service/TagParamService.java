package com.xad.server.service;

import com.xad.server.dto.TagParamDto;
import com.xad.server.entity.TagParam;

import java.util.List;

/**
 * 标签参数 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface TagParamService
{
    /**
     * 标签参数 查询记录
     * @param vo 查询条件
     * @return 标签参数
     */
    List<TagParamDto> queryTagParamList(TagParamDto vo);

    /**
     * 标签参数 根据标签ID查询记录
     * @param tagId 查询条件
     * @return 标签参数
     */
    List<TagParam> queryTagParamByTagId(Long tagId);

    /**
     * 标签参数 及其所属标签规则 新增
     */
    void insertTagParamDtoList(List<TagParamDto> tagParamDtoList);

    /**
     * 标签参数 及其所属标签规则 修改
     */
    void updateTagParamDtoList(List<TagParamDto> tagParamDtoList);

    /**
     * 标签参数 及其所属标签规则 删除
     */
    void deleteTagParamDtoList(List<TagParamDto> tagParamDtoList);

    /**
     * 标签参数 删除
     * @param vo 查询条件
     * @return 标签参数
     */
    int deleteTagParam(TagParamDto vo);

    /**
     * 标签参数规则 重建记录
     */
    void rebuildBatch(List<TagParam> tagParamList, Long tagId);

    /**
     * 标签参数规则 批量新增
     */
    void insertBatch(List<TagParam> tagParamList);

    /**
     * 标签参数规则 批量过期数据
     */
    void deprecatedBatch(List<Long> tagParamIdList);
}
