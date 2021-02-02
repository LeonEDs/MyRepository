package com.xad.server.service;

import com.xad.server.dto.TagCatalogDto;

import java.util.List;

/**
 * 标签目录 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface TagCatalogService
{
    /**
     * 查询当前标签目录的子标签目录
     * @param vo 标签目录
     * @return 标签目录信息和子标签信息
     */
    List<TagCatalogDto> queryChildTagCatalogInfo(TagCatalogDto vo);

    /**
     * 查询所有的标签目录
     * @return 标签目录信息
     */
    List<TagCatalogDto> queryAllTagCatalogInfo();

    /**
     * 查询标签目录
     * @return 标签目录信息
     */
    TagCatalogDto queryTagCatalogInfo(Long id);

    /**
     * 查询标签目录完整路径名
     * @return 标签目录路径名
     */
    String queryAllTagCatalogName(Long id);

    /**
     * 新增标签目录
     * @param vo 标签目录
     * @return 新增记录ID
     */
    Long saveTagCatalogInfo(TagCatalogDto vo);

    /**
     * 修改标签目录
     * @param vo 标签目录
     */
    int updateTagCatalogInfo(TagCatalogDto vo);

    /**
     * 新增标签目录
     * @param vo 标签目录
     */
    int deleteTagCatalogInfo(TagCatalogDto vo);
}
