package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xad.common.enums.FlowStatusEnum;
import com.xad.common.enums.StatusEnum;
import com.xad.server.dto.TagParamDto;
import com.xad.server.dto.TagParamRuleDto;
import com.xad.server.entity.TagParam;
import com.xad.server.entity.TagParamRule;
import com.xad.server.mapper.TagParamMapper;
import com.xad.server.service.TagParamRuleService;
import com.xad.server.service.TagParamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签参数 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
@Slf4j
public class TagParamServiceImpl implements TagParamService
{
    @Autowired
    TagParamMapper tagParamMapper;

    @Autowired
    TagParamRuleService tagParamRuleService;

    @Override
    public List<TagParamDto> queryTagParamList(TagParamDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getTagId(), "tagId(不能为空");

        vo.setStatus(StatusEnum.NORMAL.getCode());

        List<TagParam> tagParamList = queryTagParam(new TagParam(vo));
        List<TagParamDto> result = null;

        if (CollectionUtils.isNotEmpty(tagParamList))
        {
            result = tagParamList.stream().map(TagParamDto::new).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<TagParam> queryTagParamByTagId(Long tagId)
    {
        Assert.notNull(tagId, "入参不能为空");

        TagParam condition = new TagParam();
        condition.setTagId(tagId);
        condition.setStatus(StatusEnum.NORMAL.getCode());
        return queryTagParam(condition);
    }

    private List<TagParam> queryTagParam(TagParam tagParam)
    {
        tagParam.setParamStatus(null);

        QueryWrapper<TagParam> queryCondition = new QueryWrapper<>(tagParam);
        queryCondition.and(wrapper ->
                wrapper.ne("param_status", FlowStatusEnum.DEPRECATED.getCode())
                        .or().isNull("param_status")
        );

        return tagParamMapper.selectList(queryCondition);
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void insertTagParamDtoList(List<TagParamDto> tagParamDtoList)
    {
        if (CollectionUtils.isNotEmpty(tagParamDtoList))
        {
            for (TagParamDto param : tagParamDtoList)
            {
                Long tagId = Long.valueOf(param.getTagId());

                TagParam entity = new TagParam(param);
                entity.setId(null);
                // 新增标签参数
                tagParamMapper.insert(entity);

                Long paramId = entity.getId();
                Assert.notNull(paramId, "标签参数新增失败");

                List<TagParamRuleDto> frontRuleDtoList = param.getTagParamRuleDtoList();
                // 新增标签参数规则
                if (CollectionUtils.isNotEmpty(frontRuleDtoList))
                {
                    List<TagParamRule> insertRuleList = frontRuleDtoList.stream().map(frontRuleDto -> {
                        TagParamRule rule = new TagParamRule(frontRuleDto);
                        rule.setTagId(tagId);
                        rule.setTagParamId(paramId);
                        rule.setCreator(param.getCreator());
                        rule.setCreateTime(param.getCreateTime());
                        return rule;
                    }).collect(Collectors.toList());

                    tagParamRuleService.insertBatch(insertRuleList);
                }
            }
        }
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void updateTagParamDtoList(List<TagParamDto> tagParamDtoList)
    {
        final List<TagParamRule> insertRuleList = new ArrayList<>();
        final List<TagParamRule> updateRuleList = new ArrayList<>();
        final List<TagParamRule> deleteRuleList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(tagParamDtoList))
        {
            for (TagParamDto param : tagParamDtoList)
            {
                Long paramId = param.getId();
                Long tagId = Long.valueOf(param.getTagId());
                Assert.notNull(paramId, "标签参数ID不能为空");

                TagParam entity = new TagParam(param);
                // 修改 标签参数
                tagParamMapper.updateById(entity);

                // 前台传递的标签参数规则
                List<TagParamRuleDto> frontRuleDtoList = param.getTagParamRuleDtoList();
                // 数据库中的标签参数规则
                List<TagParamRule> dbRuleList = tagParamRuleService.queryTagParamRuleByTagParamId(paramId);
                // 数据库中的标签参数规则的ID
                List<Long> dbRuleIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(dbRuleList))
                {
                    dbRuleIdList = dbRuleList.stream().map(TagParamRule::getId).collect(Collectors.toList());
                }
                // 检索标签参数规则，新增修改删除
                if (CollectionUtils.isNotEmpty(frontRuleDtoList))
                {
                    for (TagParamRuleDto dto : frontRuleDtoList)
                    {
                        TagParamRule rule = new TagParamRule(dto);
                        rule.setTagId(tagId);
                        rule.setTagParamId(paramId);
                        // ID为空，则新增
                        if (rule.getId() == null)
                        {
                            rule.setCreator(param.getModifier());
                            rule.setCreateTime(param.getModifyTime());
                            insertRuleList.add(rule);
                        }else
                        {
                            // ID不为空，则修改
                            if (CollectionUtils.isNotEmpty(dbRuleIdList))
                            {
                                Assert.isTrue(dbRuleIdList.remove(rule.getId()), "异常标签参数规则ID");
                            }

                            rule.setModifier(param.getModifier());
                            rule.setModifyTime(param.getModifyTime());
                            updateRuleList.add(rule);
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(dbRuleIdList))
                {
                    dbRuleIdList.forEach(id -> {
                        TagParamRule rule = new TagParamRule();
                        rule.setId(id);
                        rule.setModifier(param.getModifier());
                        rule.setModifyTime(param.getModifyTime());
                        deleteRuleList.add(rule);
                    });
                }
            }
        }

        tagParamRuleService.insertBatch(insertRuleList);
        tagParamRuleService.updateBatch(updateRuleList);
        tagParamRuleService.deleteBatch(deleteRuleList);
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void deleteTagParamDtoList(List<TagParamDto> tagParamDtoList)
    {
        if (CollectionUtils.isNotEmpty(tagParamDtoList))
        {
            for (TagParamDto param : tagParamDtoList)
            {
                Long paramId = param.getId();
                Assert.notNull(paramId, "ID不能为空");

                TagParam entity = new TagParam(param);
                entity.setStatus(StatusEnum.DELETED.getCode());
                // 删除 标签参数
                tagParamMapper.updateById(entity);

                List<TagParamRule> dbRuleList = tagParamRuleService.queryTagParamRuleByTagParamId(paramId);
                // 删除 标签参数规则
                if (CollectionUtils.isNotEmpty(dbRuleList))
                {
                    dbRuleList.forEach(dbRule -> {
                        dbRule.setModifier(param.getModifier());
                        dbRule.setModifyTime(param.getModifyTime());
                    });

                    tagParamRuleService.deleteBatch(dbRuleList);
                }
            }
        }
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public int deleteTagParam(TagParamDto vo)
    {
        checkRecordExist(vo);

        vo.setStatus(StatusEnum.DELETED.getCode());
        return tagParamMapper.updateById(new TagParam(vo));
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void rebuildBatch(List<TagParam> tagParamList, Long tagId)
    {
        if (CollectionUtils.isNotEmpty(tagParamList))
        {
            for (TagParam entity : tagParamList)
            {
                Long oldParamId = entity.getId();
                entity.setId(null);
                entity.setTagId(tagId);
                tagParamMapper.insert(entity);

                Long paramId = entity.getId();
                Assert.notNull(paramId, "重建标签参数记录失败");

                List<TagParamRule> tagParamRuleList = tagParamRuleService.queryTagParamRuleByTagParamId(oldParamId);
                tagParamRuleService.rebuildBatch(tagParamRuleList, tagId, paramId);
            }
        }
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void insertBatch(List<TagParam> tagParamList)
    {
        if (CollectionUtils.isNotEmpty(tagParamList))
        {
            tagParamList.forEach(entity -> {
                entity.setId(null);
                tagParamMapper.insert(entity);
            });
        }
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void deprecatedBatch(List<Long> tagParamIdList)
    {
        if (CollectionUtils.isNotEmpty(tagParamIdList))
        {
            tagParamIdList.forEach(id -> {
                TagParam entity = new TagParam();
                entity.setId(id);
                entity.setParamStatus(FlowStatusEnum.DEPRECATED.getCode());
                tagParamMapper.updateById(entity);
            });
        }
    }

    private void checkRecordExist(TagParamDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getId(), "Id不能为空");

        TagParam simpleEntity = new TagParam();
        simpleEntity.setId(vo.getId());
        simpleEntity.setStatus(StatusEnum.NORMAL.getCode());

        Assert.isTrue(checkEntityExist(simpleEntity), "该标签参数记录不存在");
    }

    private boolean checkEntityExist(TagParam entity)
    {
        int count = tagParamMapper.selectCount(new QueryWrapper<>(entity));
        return count > 0;
    }
}
