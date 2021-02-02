package com.xad.server.jobhandler.core;

import com.xad.common.enums.ExecutionEnum;
import com.xad.common.utils.DateTimeFormatUtils;
import com.xad.server.config.ContextUtils;
import com.xad.server.dto.TagDto;
import com.xad.server.dto.TagExecutionLogDto;
import com.xad.server.entity.TagInstance;
import com.xad.server.jobhandler.ServiceInvoker;
import com.xad.server.jobhandler.handler.ParamHandler;
import com.xad.server.jobhandler.listener.event.FailedTaskEvent;
import com.xad.server.jobhandler.listener.event.SuccessTaskEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * 标签打标任务.
 *
 * 服务基础配置.
 * @version 1.0
 * @author xad
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Slf4j
public class TagMarkTask extends BaseTask
{
    private TagDto tagDto;

    private TagExecutionLogDto logDto;

    private ServiceInvoker invoker;

    public TagMarkTask(long statId, long endId, String missionCode, String parentCode, TagDto tagDto)
    {
        super(statId, endId, ExecutionEnum.SerialTask.getCode(), missionCode, parentCode);
        this.tagDto = tagDto;
        this.invoker = ContextUtils.getBean(ServiceInvoker.class);
    }



    @Override
    public void run()
    {
        log.info("Thread - SerialTask - {} - >>> starting...", this.getMissionCode());

        try
        {
            String tagValueType = this.getTagDto().getTagValueType();
            ParamHandler<TagMarkTask> handler = invoker.getParamHandlerDispatcher().dispatch(tagValueType);

            List<TagInstance> tagInstanceList = handler.handle(this);
            log.info("Thread - SerialTask - {} - >>> instance size : {}", this.getMissionCode(),
                    CollectionUtils.isNotEmpty(tagInstanceList) ? tagInstanceList.size() : 0);

            invoker.insertTagInstanceList(tagInstanceList);
            invoker.logToElastic(tagInstanceList);
//            invoker.synchDWSToEs(tagInstanceList);

            try
            {
                logDto.setExecutionResult(ExecutionEnum.SUCCESS.getCode());
                logDto.setEndTime(new Date());
                logDto.setExecutionTime(DateTimeFormatUtils.dateToMinutes(logDto.getStartTime(), logDto.getEndTime()));
                logDto.setExecutionSuccess(CollectionUtils.isNotEmpty(tagInstanceList) ?
                        (long) tagInstanceList.size() : 0L);
                invoker.sendEvent(new SuccessTaskEvent<>(this, logDto));
            }catch (Exception ignore){}
            log.info("Thread - SerialTask - {} - >>> success", this.getMissionCode());
        }catch (Exception e)
        {
            try
            {
                log.error("标签{}执行错误，串行小任务客户ID区间[{}, {}]，错误内容{}",
                        this.getTagDto().getId(), this.getPartStartId(), this.getPartEndId(), e);

                logDto.setExecutionResult(ExecutionEnum.FAILED.getCode());
                logDto.setErrorMessage(e.getMessage());
                logDto.setEndTime(new Date());
                logDto.setExecutionTime(DateTimeFormatUtils.dateToMinutes(logDto.getStartTime(), logDto.getEndTime()));
                invoker.sendEvent(new FailedTaskEvent<>(this, logDto));
            }catch (Exception ignore){}
        }
    }
}
