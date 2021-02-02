package com.xad.server.service;

import com.xad.server.entity.Dictionary;

import java.util.List;
import java.util.Map;

/**
 * 字典表 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface DictionaryService
{
    /**
     * 查询标签所有下拉框属性
     */
    Map<String, List<Dictionary>> queryTagSelectInfo();

    /**
     * 查询指定类型的标签
     */
    List<Dictionary> queryDictionaryByType(String type);

    /**
     * 查询字典表
     */
    Dictionary queryDictionary(Dictionary vo);

    /**
     * 查询板块编码
     */
    Dictionary querySectorGroup();
}
