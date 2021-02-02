package com.xad.server.jobhandler.listener;

import com.xad.common.enums.ExecutionEnum;
import com.xad.common.utils.DateTimeFormatUtils;
import com.xad.server.dto.TagExecutionLogDto;
import com.xad.server.jobhandler.listener.event.FailedTaskEvent;
import com.xad.server.jobhandler.listener.event.SuccessTaskEvent;
import com.xad.server.service.TagExecutionLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 标签任务事件.
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Component
@Slf4j
public class TagExecutionLogListener
{
    @Autowired
    TagExecutionLogService executionLogService;

    /**
     * 异步事件处理.
     * @param event 失败任务事件.
     */
    @Async
    @EventListener
    public void logFailedTaskEvent(FailedTaskEvent<TagExecutionLogDto> event)
    {
        TagExecutionLogDto logDto = event.getData();
        log.info("\n\t\t>>> failed event log: " + logDto);
        if (logDto != null)
        {
            log.info("\n\t\t>>> failed event log: " + logDto.getExecutionCode());
            executionLogService.saveAndUpdate(logDto);

            refreshParent(logDto, true);
        }
    }

    /**
     * 异步事件处理.
     * @param event 成功任务事件
     */
    @Async
    @EventListener
    public void logSuccessTaskEvent(SuccessTaskEvent<TagExecutionLogDto> event)
    {
        TagExecutionLogDto logDto = event.getData();
        log.info("\n\t\t>>> success event log: " + logDto);
        if (logDto != null)
        {
            log.info("\n\t\t>>> success event log: " + logDto.getExecutionCode());
            executionLogService.saveAndUpdate(logDto);

            refreshParent(logDto, false);
        }
    }

    private void refreshParent(TagExecutionLogDto logDto, boolean isFail)
    {
        if (logDto != null && StringUtils.isNotEmpty(logDto.getParentCode()))
        {
            TagExecutionLogDto dbRecord = executionLogService.queryExecutionLogInfo(logDto.getParentCode());
            if (dbRecord != null)
            {
                if (isFail)
                {
                    dbRecord.setExecutionResult(ExecutionEnum.FAILED.getCode());
                }else if (!ExecutionEnum.FAILED.getCode().equals(dbRecord.getExecutionResult()))
                {
                    dbRecord.setExecutionResult(ExecutionEnum.SUCCESS.getCode());
                }
                dbRecord.setEndTime(logDto.getEndTime());
                dbRecord.setExecutionTime(DateTimeFormatUtils.dateToMinutes(dbRecord.getStartTime(), dbRecord.getEndTime()));
                executionLogService.saveAndUpdate(dbRecord);
            }
        }
    }
}
