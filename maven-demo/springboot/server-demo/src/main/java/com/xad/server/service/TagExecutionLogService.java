package com.xad.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xad.server.dto.RequestPage;
import com.xad.server.dto.TagExecutionLogDto;

import java.util.List;

/**
 * 标签执行日志 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface TagExecutionLogService
{
    /**
     * 标签执行日志 分页查询(打标总任务)
     */
    Page<TagExecutionLogDto> queryExecutionLogPage(RequestPage<TagExecutionLogDto> vo);

    /**
     * 标签执行日志 查询链表
     */
    List<TagExecutionLogDto> queryExecutionLogList(TagExecutionLogDto dto);

    /**
     * 标签执行日志 查询失败的切片任务
     */
    List<TagExecutionLogDto> queryFailedTaskList(TagExecutionLogDto dto);

    /**
     * 标签执行日志 根据执行编码查询记录
     */
    TagExecutionLogDto queryExecutionLogInfo(String executionCode);

    /**
     * 标签执行日志 新建
     */
    TagExecutionLogDto saveAndUpdate(TagExecutionLogDto dto);

    /**
     * 标签执行日志 重新执行任务
     */
    TagExecutionLogDto reExecute(TagExecutionLogDto dto);

    /**
     * 标签执行日志 批量（所有失败的第二级任务）重新执行任务
     */
    TagExecutionLogDto reExecuteBatch(TagExecutionLogDto dto);
}
