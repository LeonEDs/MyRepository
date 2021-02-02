package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xad.common.enums.FlowStatusEnum;
import com.xad.common.enums.StatusEnum;
import com.xad.common.enums.TagEnum;
import com.xad.server.dto.TagOperationLogDto;
import com.xad.server.entity.TagOperationLog;
import com.xad.server.mapper.TagOperationLogMapper;
import com.xad.server.service.TagOperationLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签和标签策略操作日志 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class TagOperationLogServiceImpl implements TagOperationLogService
{
    @Autowired
    TagOperationLogMapper tagOperationLogMapper;

    @Override
    public List<TagOperationLogDto> queryOperationLogList(TagOperationLogDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getObjType(), "objType不能为空");
        Assert.notNull(vo.getObjCode(), "objCode不能为空");

        vo.setStatus(StatusEnum.NORMAL.getCode());
        List<TagOperationLog> data = tagOperationLogMapper.selectList(new QueryWrapper<>(new TagOperationLog(vo)));
        List<TagOperationLogDto> result = null;

        if (CollectionUtils.isNotEmpty(data))
        {
            result = data.stream().map(TagOperationLogDto::new).collect(Collectors.toList());
            result.forEach(logDto -> logDto.setOperationName(FlowStatusEnum.getName(logDto.getOperationType())));
        }
        return result;
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void saveTagOperationLog(String tagCode, Float version, String description, String operationType
            , String username, Date createTime)
    {
        Assert.notNull(tagCode, "tagCode不能为空");

        TagOperationLogDto vo = creatTagOperationLog(tagCode, version, TagEnum.ObjType.TAG_LOG.getCode(),
                description, operationType, username, createTime);
        tagOperationLogMapper.insert(new TagOperationLog(vo));
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void saveTagStrategyOperationLog(String tagStrategyCode, Float version, String description, String operationType
            , String username, Date createTime)
    {
        Assert.notNull(tagStrategyCode, "tagStrategyCode不能为空");

        TagOperationLogDto vo = creatTagOperationLog(tagStrategyCode, version, TagEnum.ObjType.TAG_STRATEGY_LOG.getCode(),
                description, operationType, username, createTime);
        tagOperationLogMapper.insert(new TagOperationLog(vo));
    }

    private static TagOperationLogDto creatTagOperationLog(String code, Float version, String objType, String description
            , String operationType, String username, Date createTime)
    {
        TagOperationLogDto dto = new TagOperationLogDto();
        dto.setObjCode(code);
        dto.setObjType(objType);
        dto.setOperationType(operationType);
        dto.setVersion(version);
        dto.setDescription(description);
        dto.setCreator(username);
        dto.setCreateTime(createTime);

        return dto;
    }
}
