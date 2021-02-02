package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xad.common.enums.FlowStatusEnum;
import com.xad.common.enums.StatusEnum;
import com.xad.server.dto.TagParamRuleDto;
import com.xad.server.entity.TagParamRule;
import com.xad.server.mapper.TagParamRuleMapper;
import com.xad.server.service.TagParamRuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签参数规则 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class TagParamRuleServiceImpl implements TagParamRuleService
{
    @Autowired
    TagParamRuleMapper tagParamRuleMapper;

    @Override
    public List<TagParamRuleDto> queryTagParamRuleList(TagParamRuleDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getTagId(), "tagId不能为空");

        vo.setStatus(StatusEnum.NORMAL.getCode());
        vo.setRuleStatus(null);

        List<TagParamRule> data = queryByCondition(new TagParamRule(vo));
        List<TagParamRuleDto> result = null;

        if (CollectionUtils.isNotEmpty(data))
        {
            result = data.stream().map(TagParamRuleDto::new).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<TagParamRule> queryTagParamRuleByTagId(Long tagId)
    {
        if (tagId != null)
        {
            TagParamRule condition = new TagParamRule();
            condition.setTagId(tagId);
            condition.setStatus(StatusEnum.NORMAL.getCode());
            return queryByCondition(condition);
        }
        return null;
    }

    @Override
    public List<TagParamRule> queryTagParamRuleByTagParamId(Long tagParamId)
    {
        if (tagParamId != null)
        {
            TagParamRule condition = new TagParamRule();
            condition.setTagParamId(tagParamId);
            condition.setStatus(StatusEnum.NORMAL.getCode());
            return queryByCondition(condition);
        }
        return null;
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void rebuildBatch(List<TagParamRule> tagParamRuleList, Long tagId, Long paramId)
    {
        if (CollectionUtils.isNotEmpty(tagParamRuleList))
        {
            for (TagParamRule entity : tagParamRuleList)
            {
                entity.setId(null);
                entity.setTagId(tagId);
                entity.setTagParamId(paramId);
                tagParamRuleMapper.insert(entity);
            }
        }
    }

    public List<TagParamRule> queryByCondition(TagParamRule condition)
    {
        QueryWrapper<TagParamRule> queryCondition = new QueryWrapper<>(condition);
        queryCondition.and(wrapper ->
                wrapper.ne("rule_status", FlowStatusEnum.DEPRECATED.getCode())
                        .or().isNull("rule_status")
        );

        return tagParamRuleMapper.selectList(queryCondition);
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void insertBatch(List<TagParamRule> tagParamRuleList)
    {
        if (CollectionUtils.isNotEmpty(tagParamRuleList))
        {
            tagParamRuleList.forEach(entity -> {
                if (entity != null && entity.getTagId() != null && entity.getTagParamId() != null)
                {
                    entity.setId(null);
                    tagParamRuleMapper.insert(entity);
                }
            });

        }
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void updateBatch(List<TagParamRule> tagParamRuleList)
    {
        if (CollectionUtils.isNotEmpty(tagParamRuleList))
        {
            tagParamRuleList.forEach(entity -> {
                if (entity != null && entity.getId() != null)
                {
                    tagParamRuleMapper.updateById(entity);
                }
            });
        }
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void deleteBatch(List<TagParamRule> tagParamRuleList)
    {
        if (CollectionUtils.isNotEmpty(tagParamRuleList))
        {
            tagParamRuleList.forEach(entity -> {
                if (entity != null && entity.getId() != null)
                {
                    entity.setStatus(StatusEnum.DELETED.getCode());
                    tagParamRuleMapper.updateById(entity);
                }
            });
        }
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void deprecatedBatch(List<Long> tagParamRuleIdList)
    {
        if (CollectionUtils.isNotEmpty(tagParamRuleIdList))
        {
            tagParamRuleIdList.forEach(id -> {
                TagParamRule entity = new TagParamRule();
                entity.setId(id);
                entity.setRuleStatus(FlowStatusEnum.DEPRECATED.getCode());
                tagParamRuleMapper.updateById(entity);
            });
        }
    }

    private boolean checkRecordExist(TagParamRule vo)
    {
        TagParamRule simpleEntity = new TagParamRule();
        simpleEntity.setId(vo.getId());
        simpleEntity.setStatus(StatusEnum.NORMAL.getCode());

        return checkEntityExist(simpleEntity);
    }

    private boolean checkEntityExist(TagParamRule entity)
    {
        int count = tagParamRuleMapper.selectCount(new QueryWrapper<>(entity));
        return count > 0;
    }
}
