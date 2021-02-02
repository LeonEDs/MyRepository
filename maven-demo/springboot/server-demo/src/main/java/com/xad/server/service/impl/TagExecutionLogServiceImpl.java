package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xad.common.enums.ExecutionEnum;
import com.xad.common.enums.StatusEnum;
import com.xad.server.dto.RequestPage;
import com.xad.server.dto.TagDto;
import com.xad.server.dto.TagExecutionLogDto;
import com.xad.server.entity.TagEntity;
import com.xad.server.entity.TagExecutionLog;
import com.xad.server.jobhandler.executor.TagTaskExecutor;
import com.xad.server.mapper.TagExecutionLogMapper;
import com.xad.server.service.TagExecutionLogService;
import com.xad.server.service.TagManagerService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签执行日志 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class TagExecutionLogServiceImpl implements TagExecutionLogService
{
    @Autowired
    TagExecutionLogMapper tagExecutionLogMapper;

    @Autowired
    TagManagerService tagManagerService;

    @Autowired
    TagTaskExecutor executor;

    @Override
    public Page<TagExecutionLogDto> queryExecutionLogPage(RequestPage<TagExecutionLogDto> vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getVo(), "查询条件不能为空");
        Assert.notNull(vo.getPage(), "分页参数不能为空");

        TagExecutionLogDto conditionVo = vo.getVo();
        conditionVo.setExecutionType(ExecutionEnum.MarkTask.getCode());
        QueryWrapper<TagExecutionLog> queryWrapper = fuzzyQuery(conditionVo);

        Page<TagExecutionLog> pageCondition = new Page<>(vo.getPage().getCurrent(), vo.getPage().getSize());
        Page<TagExecutionLogDto> result = new Page<>(vo.getPage().getCurrent(), vo.getPage().getSize());
        int count = 0;
        if (queryWrapper == null)
        {
            result.setTotal(count);
            result.setRecords(null);
            return result;
        }
        count = tagExecutionLogMapper.selectCount(queryWrapper);
        pageCondition.setTotal(count);
        result.setTotal(count);

        IPage<TagExecutionLog> logPage = tagExecutionLogMapper.selectPage(pageCondition, queryWrapper);
        List<TagExecutionLogDto> logList = null;
        if (logPage != null && CollectionUtils.isNotEmpty(logPage.getRecords()))
        {
            logList = logPage.getRecords().stream().map(TagExecutionLogDto::new).collect(Collectors.toList());
            logList.forEach(log -> {
                Long tagId = Long.valueOf(log.getTagId());
                TagDto tagDto = tagManagerService.queryTagInfo(tagId);
                log.setTagDto(tagDto);

                log.setExecutionResult(ExecutionEnum.getName(log.getExecutionResult()));
            });
        }

        result.setRecords(logList);
        return result;
    }

    @Override
    public List<TagExecutionLogDto> queryExecutionLogList(TagExecutionLogDto dto)
    {
        QueryWrapper<TagExecutionLog> queryWrapper = fuzzyQuery(dto);

        if (queryWrapper == null)
        {
            return null;
        }

        List<TagExecutionLog> list = tagExecutionLogMapper.selectList(queryWrapper);
        List<TagExecutionLogDto> result = null;
        if (CollectionUtils.isNotEmpty(list))
        {
            result = list.stream().map(TagExecutionLogDto::new).collect(Collectors.toList());
            result.forEach(log -> {
                Long tagId = Long.valueOf(log.getTagId());
                TagDto tagDto = tagManagerService.queryTagInfo(tagId);
                log.setTagDto(tagDto);

                log.setExecutionResult(ExecutionEnum.getName(log.getExecutionResult()));
            });
        }
        return result;
    }

    @Override
    public List<TagExecutionLogDto> queryFailedTaskList(TagExecutionLogDto dto)
    {
        Assert.notNull(dto.getExecutionCode(), "任务executionCode不能为空");
        List<TagExecutionLogDto> result = new ArrayList<>();

        QueryWrapper<TagExecutionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_code", dto.getExecutionCode());

        List<TagExecutionLog> entityList = tagExecutionLogMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(entityList))
        {
            entityList.forEach(entity -> {
                Long tagId = entity.getTagId();
                TagDto tagDto = tagManagerService.queryTagInfo(tagId);

                QueryWrapper<TagExecutionLog> querySubWrapper = new QueryWrapper<>();
                querySubWrapper.eq("execution_type", ExecutionEnum.SerialTask.getCode());
                querySubWrapper.eq("parent_code", entity.getExecutionCode());
                querySubWrapper.eq("execution_result", ExecutionEnum.FAILED.getCode());
                List<TagExecutionLog> subEntityList = tagExecutionLogMapper.selectList(querySubWrapper);
                if (CollectionUtils.isNotEmpty(subEntityList))
                {
                    result.addAll(subEntityList.stream().map(subEntity -> {
                        TagExecutionLogDto logDto = new TagExecutionLogDto(subEntity);
                        logDto.setTagDto(tagDto);
                        logDto.setExecutionResult(ExecutionEnum.getName(logDto.getExecutionResult()));
                        return logDto;
                    }).collect(Collectors.toList()));
                }

                if (ExecutionEnum.FAILED.getCode().equals(entity.getExecutionResult()))
                {
                    TagExecutionLogDto logDto = new TagExecutionLogDto(entity);
                    logDto.setTagDto(tagDto);
                    logDto.setExecutionResult(ExecutionEnum.getName(logDto.getExecutionResult()));
                    result.add(logDto);
                }
            });
        }

        return result;
    }

    @Override
    public TagExecutionLogDto queryExecutionLogInfo(String executionCode)
    {
        if (StringUtils.isNotEmpty(executionCode))
        {
            QueryWrapper<TagExecutionLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("execution_code", executionCode);
            TagExecutionLog entity = tagExecutionLogMapper.selectOne(queryWrapper);
            if (entity != null)
            {
                return new TagExecutionLogDto(entity);
            }
        }
        return null;
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public TagExecutionLogDto saveAndUpdate(TagExecutionLogDto dto)
    {
        TagExecutionLog logEntity = new TagExecutionLog(dto);

        QueryWrapper<TagExecutionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("execution_code", logEntity.getExecutionCode());
        TagExecutionLog dbRecord = tagExecutionLogMapper.selectOne(queryWrapper);
        if (dbRecord != null)
        {
            logEntity.setId(dbRecord.getId());
            tagExecutionLogMapper.updateById(logEntity);
        }else
        {
            tagExecutionLogMapper.insert(logEntity);
            Assert.notNull(logEntity.getId(), "新建标签执行记录失败" + dto.getExecutionCode());
        }
        return new TagExecutionLogDto(logEntity);
    }

    @Override
    public TagExecutionLogDto reExecute(TagExecutionLogDto dto)
    {
        Assert.notNull(dto, "入参不能为空");
        Assert.notNull(dto.getId(), "Id不能为空");
        QueryWrapper<TagExecutionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", dto.getId());

        TagExecutionLog executionLog = tagExecutionLogMapper.selectOne(queryWrapper);
        Assert.notNull(executionLog, "该标签执行记录不存在");
        Assert.isTrue(ExecutionEnum.FAILED.getCode().equals(executionLog.getExecutionResult()), "该标签任务无需重新执行");

        List<TagExecutionLog> list = new ArrayList<>(1);
        list.add(executionLog);

        return new TagExecutionLogDto(executionLog);
    }

    @Override
    public TagExecutionLogDto reExecuteBatch(TagExecutionLogDto dto)
    {
        Assert.notNull(dto, "入参不能为空");
        Assert.notNull(dto.getExecutionCode(), "executionCode不能为空");

        QueryWrapper<TagExecutionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_code", dto.getExecutionCode());

        List<TagExecutionLog> entityList = tagExecutionLogMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(entityList))
        {
            List<TagExecutionLog> reExecuteList = new LinkedList<>();
            entityList.forEach(entity -> {
                QueryWrapper<TagExecutionLog> querySubWrapper = new QueryWrapper<>();
                querySubWrapper.eq("execution_type", ExecutionEnum.SerialTask.getCode());
                querySubWrapper.eq("parent_code", entity.getExecutionCode());
                querySubWrapper.eq("execution_result", ExecutionEnum.FAILED.getCode());
                int subEntitySize = tagExecutionLogMapper.selectCount(querySubWrapper);

                if (subEntitySize > 0)
                {
                    // 如果第二级任务有失败的第三级任务，则重新执行该第二级任务
                    reExecuteList.add(entity);
                }
            });
        }

        return dto;
    }

    private QueryWrapper<TagExecutionLog> fuzzyQuery(TagExecutionLogDto dto)
    {
        QueryWrapper<TagExecutionLog> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("status", StatusEnum.NORMAL.getCode());

        List<Long> tagIdList = null;

        QueryWrapper<TagEntity> queryTagWrapper = new QueryWrapper<>();

        String tagName = dto.getTagName();
        String tagModule = dto.getTagModule();
        String tagModuleCode = dto.getTagModuleCode();
        boolean isQuery = false;

        if (StringUtils.isNotEmpty(tagName) && StringUtils.isNotEmpty(tagName.trim()))
        {
            queryTagWrapper.like("tag_name", tagName.trim());
            isQuery = true;
        }
        if (StringUtils.isNotEmpty(tagModule) && StringUtils.isNotEmpty(tagModule.trim()))
        {
            queryTagWrapper.eq("tag_module", tagModule.trim());
            isQuery = true;
        }
        if (StringUtils.isNotEmpty(tagModuleCode) && StringUtils.isNotEmpty(tagModuleCode.trim()))
        {
            queryTagWrapper.eq("tag_module_code", tagModuleCode.trim());
            isQuery = true;
        }

        if (isQuery)
        {
            List<TagEntity> tagList = tagManagerService.selectList(queryTagWrapper);
            if (CollectionUtils.isNotEmpty(tagList))
            {
                tagIdList = tagList.stream().map(TagEntity::getId).collect(Collectors.toList());
            }else
            {
                return null;
            }
        }

        if (CollectionUtils.isNotEmpty(tagIdList))
        {
            queryWrapper.in("tag_id", tagIdList);
        }

        String executionCode = dto.getExecutionCode();
        String parentCode = dto.getParentCode();
        String executionType = dto.getExecutionType();
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        String executionResult = dto.getExecutionResult();
        Long id = dto.getId();
        String tagCode = dto.getTagCode();

        if (StringUtils.isNotEmpty(executionType) && StringUtils.isNotEmpty(executionType.trim()))
        {
            queryWrapper.eq("execution_type", executionType.trim());
        }
        if (StringUtils.isNotEmpty(executionCode) && StringUtils.isNotEmpty(executionCode.trim()))
        {
            queryWrapper.eq("execution_code", executionCode.trim());
        }
        if (StringUtils.isNotEmpty(parentCode) && StringUtils.isNotEmpty(parentCode.trim()))
        {
            queryWrapper.eq("parent_code", parentCode.trim());
        }
        if (startTime != null)
        {
            queryWrapper.apply("DATE_FORMAT(start_time,'%Y-%m-%d') >= DATE_FORMAT({0},'%Y-%m-%d')", startTime);
        }
        if (endTime != null)
        {
            queryWrapper.apply("DATE_FORMAT(start_time,'%Y-%m-%d') <= DATE_FORMAT({0},'%Y-%m-%d')", endTime);
        }
        if (StringUtils.isNotEmpty(executionResult) && StringUtils.isNotEmpty(executionResult.trim()))
        {
            queryWrapper.eq("execution_result", executionResult.trim());
        }
        if (StringUtils.isNotEmpty(dto.getTagId()))
        {
            queryWrapper.eq("tag_id", dto.getTagId());
        }
        if (id != null)
        {
            queryWrapper.eq("id", id);
        }
        if (StringUtils.isNotEmpty(tagCode) && StringUtils.isNotEmpty(tagCode.trim()))
        {
            queryWrapper.eq("tag_code", tagCode.trim());
        }
        queryWrapper.orderByDesc("start_time");

        return queryWrapper;
    }
}
