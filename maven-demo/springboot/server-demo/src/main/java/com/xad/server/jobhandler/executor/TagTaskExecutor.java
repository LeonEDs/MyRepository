package com.xad.server.jobhandler.executor;

import com.xad.server.jobhandler.ServiceInvoker;
import com.xad.server.jobhandler.core.TagMarkTask;
import com.xad.common.enums.ExecutionEnum;
import com.xad.server.dto.TagDto;
import com.xad.server.dto.TagExecutionLogDto;
import com.xad.server.jobhandler.JobHandlerConstants;
import com.xad.server.service.TagInstanceService;
import com.xad.server.service.TagManagerService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 标签执行器.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Component
public class TagTaskExecutor
{
    @Autowired
    TagManagerService tagManagerService;

    @Autowired
    TagInstanceService tagInstanceService;

    @Autowired
    ServiceInvoker serviceInvoker;

    private final TaskExecutorService<TagMarkTask> taskExecutorService;

    public TagTaskExecutor()
    {
        this.taskExecutorService = new TaskExecutorService<>();
    }

    /**
     * 执行.
     * @param tagId 标签ID
     */
    public void execute(Long tagId) throws InterruptedException
    {
        TagDto queryCondition = new TagDto();
        queryCondition.setId(String.valueOf(tagId));
        TagDto tagDto =  tagManagerService.queryDetailsById(queryCondition);
        // 校验标签合法性
//        validTagInfo(tagDto);

        // 生成任务
        List<TagMarkTask> markTaskList = generateTask(tagDto);
        if (CollectionUtils.isNotEmpty(markTaskList))
        {
            // 清除旧的标签实例记录
            tagInstanceService.deleteBatchByTagCode(tagDto.getTagCode());

            // 提交任务
            taskExecutorService.putTaskList(markTaskList);
        }
    }

    /**
     * 根据客户ID最大值和最小值，切片生成任务.
     * @param tagDto 标签详细信息
     */
    private List<TagMarkTask> generateTask(TagDto tagDto)
    {
        Long maxCustId = tagInstanceService.getMaxCustId();
        Long minCustId = tagInstanceService.getMinCustId();
        String missionCode = generateMissionCode();

        if (maxCustId != null && minCustId != null && maxCustId > 0 && minCustId > 0)
        {
            return splitTask(minCustId, maxCustId, tagDto, missionCode);
        }

        return null;
    }

    /**
     * 1000个客户为一组组成小任务.
     */
    public List<TagMarkTask> splitTask(long startId, long endId, TagDto tagDto, String missionCode)
    {
        long partLength = endId - startId;
        long newStart = startId;
        long newEnd = startId;
        int serialNum = 1;
        List<TagMarkTask> tagMarkTaskList = new LinkedList<>();

        if (partLength >= 0 && endId > 0 && startId > 0)
        {
            TagExecutionLogDto logDto = createExecLogDto(tagDto, missionCode, null
                    , ExecutionEnum.MarkTask.getCode(), startId, endId);
            serviceInvoker.saveOrUpdateExecLog(logDto);
            do
            {
                newEnd += (JobHandlerConstants.MAX_SERIAL_TASK_NUM - 1);
                String taskMissionCode = missionCode + "#" + serialNum++;
                TagMarkTask task;
                if (newEnd > endId)
                {
                    logDto = createExecLogDto(tagDto, taskMissionCode, missionCode, ExecutionEnum.SerialTask.getCode()
                            , newStart, endId);
                    task = new TagMarkTask(newStart, endId, taskMissionCode, missionCode, tagDto);

                    serviceInvoker.saveOrUpdateExecLog(logDto);
                    tagMarkTaskList.add(task);

                    break;
                }else
                {
                    logDto = createExecLogDto(tagDto, taskMissionCode, missionCode, ExecutionEnum.SerialTask.getCode()
                            , newStart, newEnd);
                    task = new TagMarkTask(newStart, newEnd, taskMissionCode, missionCode, tagDto);

                    serviceInvoker.saveOrUpdateExecLog(logDto);
                    tagMarkTaskList.add(task);

                    newStart = newEnd + 1;
                    newEnd = newStart;
                }
            }while (newEnd <= endId);
        }

        return tagMarkTaskList;
    }

    private TagExecutionLogDto createExecLogDto(TagDto tagDto, String missionCode, String parentCode, String taskType
            , Long startId, Long endId)
    {
        TagExecutionLogDto logDto = new TagExecutionLogDto();
        logDto.setTagId(tagDto.getId());
        logDto.setTagCode(tagDto.getTagCode());
        logDto.setExecutionCode(missionCode);
        logDto.setParentCode(parentCode);
        logDto.setExecutionType(taskType);
        logDto.setCustIdStart(startId);
        logDto.setCustIdEnd(endId);
        logDto.setStartTime(new Date());

        return logDto;
    }

    /**
     * 生成任务编号.
     */
    private String generateMissionCode()
    {
        return "EX" + System.currentTimeMillis();
    }

    public boolean isFinish()
    {
        return this.taskExecutorService.isFinish();
    }
}
