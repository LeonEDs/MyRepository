package com.xad.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xad.server.dto.RequestPage;
import com.xad.server.dto.TagDto;
import com.xad.server.entity.TagEntity;

import java.util.List;

/**
 * 标签管理 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface TagManagerService
{
    /**
     * 根据标签属性条件查询所有标签
     * @param vo 查询条件
     * @return 标签
     */
    List<TagDto> queryTagsByCondition(TagDto vo);

    /**
     * 根据标签属性条件查询标签分页
     * @param vo 查询条件
     * @return 标签
     */
    Page<TagDto> queryTagsPage(RequestPage<TagDto> vo);

    /**
     * 标签 查询详细信息（标签信息、标签参数信息、标签参数规则信息）
     * @return 标签
     */
    TagDto queryDetailsById(TagDto vo);

    TagDto queryDetailsByCode(String tagCode);

    /**
     * 标签 查询基本信息
     * @return 标签
     */
    TagDto queryTagInfo(Long tagId);

    /**
     * 自定义查询条件
     * @return 标签
     */
    List<TagEntity> selectList(QueryWrapper<TagEntity> wrapper);

    /**
     * 标签 新增 前置动作生成标签编码
     * @return 标签
     */
    TagDto preAddTag(TagDto vo);

    /**
     * 标签 修改 前置动作查询详情，已发布标签重建记录
     * @return 标签
     */
    TagDto preUpdateTag(TagDto vo);

    /**
     * 标签 新增
     * @param vo 查询条件
     */
    TagDto saveTagInfo(TagDto vo);

    /**
     * 标签 修改
     * @param vo 查询条件
     */
    TagDto updateTagInfo(TagDto vo, TagEntity dbRecord);

    /**
     * 标签 过期版本
     * @param vo 查询条件
     */
    void deprecatedTag(TagDto vo);
}
