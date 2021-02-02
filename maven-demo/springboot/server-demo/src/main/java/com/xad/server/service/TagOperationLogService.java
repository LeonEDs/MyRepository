package com.xad.server.service;

import com.xad.server.dto.TagOperationLogDto;

import java.util.Date;
import java.util.List;

/**
 * 标签和标签策略操作日志 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface TagOperationLogService
{
    /**
     * 标签和标签策略操作日志 查询
     * @param vo 查询条件
     * @return 操作日志
     */
    List<TagOperationLogDto> queryOperationLogList(TagOperationLogDto vo);
    /**
     * 标签和标签策略操作日志 新建
     */
    void saveTagOperationLog(String tagCode, Float version, String description, String operationType, String username, Date createTime);
    /**
     * 标签和标签策略操作日志 新建
     */
    void saveTagStrategyOperationLog(String strategyCode, Float version, String description, String operationType, String username, Date createTime);
}
