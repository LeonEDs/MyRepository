package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xad.common.constant.DateTimeConstant;
import com.xad.common.enums.FlowStatusEnum;
import com.xad.common.enums.StatusEnum;
import com.xad.common.enums.TagEnum;
import com.xad.server.dto.RequestPage;
import com.xad.server.dto.TagDto;
import com.xad.server.dto.TagParamDto;
import com.xad.server.dto.TagParamRuleDto;
import com.xad.server.entity.Dictionary;
import com.xad.server.entity.TagEntity;
import com.xad.server.entity.TagParam;
import com.xad.server.entity.TagParamRule;
import com.xad.server.mapper.TagEntityMapper;
import com.xad.server.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签管理 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class TagManagerServiceImpl implements TagManagerService
{
    @Autowired
    TagEntityMapper tagEntityMapper;

    @Autowired
    TagCatalogService tagCatalogService;

    @Autowired
    TagParamService tagParamService;

    @Autowired
    TagParamRuleService tagParamRuleService;

    @Autowired
    TagOperationLogService tagOperationLogService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    TagInstanceService tagInstanceService;

    @Override
    public List<TagDto> queryTagsByCondition(TagDto vo)
    {
        Assert.notNull(vo, "入参不能为空");

        QueryWrapper<TagEntity> queryCondition = fuzzyQuery(vo);

        List<TagEntity> resultEntity = tagEntityMapper.selectList(queryCondition);

        List<TagDto> result = null;
        if (CollectionUtils.isNotEmpty(resultEntity))
        {
            result = resultEntity.stream().map(TagDto::new).collect(Collectors.toList());
            result.forEach(dto -> {
                List<TagParam> tagParamList = tagParamService.queryTagParamByTagId(Long.valueOf(dto.getId()));
                if (CollectionUtils.isNotEmpty(tagParamList))
                {
                    List<TagParamDto> tagParamDtoList = tagParamList.stream().map(TagParamDto::new).collect(Collectors.toList());
                    dto.setTagParamDtoList(tagParamDtoList);
                }
            });
        }
        return result;
    }

    @Override
    public Page<TagDto> queryTagsPage(RequestPage<TagDto> vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getVo(), "查询条件不能为空");
        Assert.notNull(vo.getPage(), "分页参数不能为空");

        QueryWrapper<TagEntity> queryCondition = fuzzyQuery(vo.getVo());

        Page<TagEntity> pageCondition = new Page<>(vo.getPage().getCurrent(), vo.getPage().getSize());
        Page<TagDto> result = new Page<>(vo.getPage().getCurrent(), vo.getPage().getSize());
        int count = tagEntityMapper.selectCount(queryCondition);
        pageCondition.setTotal(count);
        result.setTotal(count);

        IPage<TagEntity> pageResult = tagEntityMapper.selectPage(pageCondition, queryCondition);
        List<TagDto> dataDtoList;
        if (CollectionUtils.isNotEmpty(pageResult.getRecords()))
        {
            dataDtoList = pageResult.getRecords().stream().map(TagDto::new).collect(Collectors.toList());
            dataDtoList.forEach(this::reverseCodeName);
            result.setRecords(dataDtoList);
        }

        return result;
    }

    @Override
    public TagDto queryDetailsById(TagDto vo)
    {
        TagEntity entity = queryTagEntityById(vo);

        return queryDetail(entity);
    }

    @Override
    public TagDto queryDetailsByCode(String tagCode)
    {
        QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_code", tagCode);
        queryWrapper.eq("tag_status", FlowStatusEnum.PUBLISH.getCode());
        TagEntity entity = tagEntityMapper.selectOne(queryWrapper);

        return queryDetail(entity);
    }

    private TagDto queryDetail(TagEntity entity)
    {
        TagDto result = new TagDto(entity);
        List<TagParam> tagParamList = tagParamService.queryTagParamByTagId(entity.getId());
        if (CollectionUtils.isNotEmpty(tagParamList))
        {
            List<TagParamDto> tagParamDtoList = tagParamList.stream().map(TagParamDto::new).collect(Collectors.toList());
            tagParamDtoList.forEach(dto -> {
                List<TagParamRule> tagParamRuleList = tagParamRuleService.queryTagParamRuleByTagParamId(dto.getId());
                if (CollectionUtils.isNotEmpty(tagParamRuleList))
                {
                    List<TagParamRuleDto> tagParamRuleDtoList = tagParamRuleList.stream().map(TagParamRuleDto::new).collect(Collectors.toList());
                    dto.setTagParamRuleDtoList(tagParamRuleDtoList);
                }
            });
            result.setTagParamDtoList(tagParamDtoList);
        }

        // 标签目录全路径名
        String tagCatalogName = tagCatalogService.queryAllTagCatalogName(Long.valueOf(result.getTagCatalogId()));
        result.setTagAllCatalogName(tagCatalogName);
        if (TagEnum.TagModule.ENTERPRISE.getValue().equals(result.getTagModule()))
        {
            result.setBkCode(organizationService.querySectorByEnterprise(result.getTagModuleCode()));
        }

        return result;
    }

    @Override
    public TagDto queryTagInfo(Long tagId)
    {
        TagDto tagDto = null;
        if (tagId != null)
        {
            TagEntity entity = tagEntityMapper.selectById(tagId);
            tagDto = new TagDto(entity);

            reverseCodeName(tagDto);
        }
        return tagDto;
    }

    @Override
    public List<TagEntity> selectList(QueryWrapper<TagEntity> wrapper)
    {
        wrapper.eq("status", StatusEnum.NORMAL.getCode());
        // 排除版本已过期的记录
        wrapper.ne("tag_status", FlowStatusEnum.DEPRECATED.getCode());
        return tagEntityMapper.selectList(wrapper);
    }

    @Override
    public TagDto preAddTag(TagDto vo)
    {
        TagEntity tagEntity = new TagEntity(vo);
        tagEntity.setTagCode(generateTagCode());

        return new TagDto(tagEntity);
    }

    @Override
    public TagDto preUpdateTag(TagDto vo)
    {
        Assert.notNull(vo, "入参不能为空");
        Assert.notNull(vo.getId(), "标签Id不能为空");
        Assert.isTrue(StringUtils.isNotEmpty(vo.getTagCode()), "标签编码不能为空");
        Assert.notNull(StringUtils.isNotEmpty(vo.getTagCode().trim()), "标签编码不能为空");

        TagDto detailCondition = new TagDto();
        QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_code", vo.getTagCode());
        // 查找对应修改中的标签记录
        queryWrapper.eq("tag_status", FlowStatusEnum.MODIFY.getCode());
        TagEntity entity = tagEntityMapper.selectOne(queryWrapper);
        if (entity != null && !StringUtils.equals(String.valueOf(entity.getId()), vo.getId()))
        {
            detailCondition.setId(String.valueOf(entity.getId()));
        }else
        {
            detailCondition.setId(vo.getId());
        }
        return queryDetailsById(detailCondition);
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public TagDto saveTagInfo(TagDto vo)
    {
        //新增记录，状态改为修改中，设置版本号为 1
        vo.setTagStatus(FlowStatusEnum.MODIFY.getCode());
        vo.setVersion(1f);
        vo.setCreator(vo.getModifier());
        vo.setCreateTime(vo.getModifyTime());
        vo.setModifier(null);
        vo.setModifyTime(null);
        TagEntity entity = new TagEntity(vo);
        if (TagEnum.TagModule.GROUP.getValue().equals(entity.getTagModule()))
        {
            Dictionary dic = dictionaryService.querySectorGroup();
            entity.setTagModuleCode(dic == null ? "1" : dic.getVal());
        }
        tagEntityMapper.insert(entity);

        Long tagId = entity.getId();
        Assert.isTrue(tagId != null, "标签新增失败！");

        //新增标签参数 和 标签参数规则
        if (CollectionUtils.isNotEmpty(vo.getTagParamDtoList()))
        {
            List<TagParamDto> insertParamList = vo.getTagParamDtoList();
            insertParamList.forEach(param -> {
                param.setCreator(vo.getCreator());
                param.setCreateTime(vo.getCreateTime());
                param.setTagId(String.valueOf(tagId));
            });
            tagParamService.insertTagParamDtoList(insertParamList);
        }

        //记录操作记录
        tagOperationLogService.saveTagOperationLog(entity.getTagCode(), entity.getVersion(),
                vo.getOperateContent(), FlowStatusEnum.ADD.getCode(), vo.getCreator(), vo.getCreateTime());

        return new TagDto(entity);
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public TagDto updateTagInfo(TagDto vo, TagEntity dbRecord)
    {
        // 修改记录，状态不变，版本号不变
        Long tagId = Long.valueOf(vo.getId());

        TagEntity entity = new TagEntity(vo);
        tagEntityMapper.updateById(entity);

        // 前台传递的标签参数
        List<TagParamDto> frontParamDtoList = vo.getTagParamDtoList();
        // 数据库已存的标签参数
        List<TagParam> dbParamList = tagParamService.queryTagParamByTagId(tagId);
        // 数据库已存的标签参数的ID
        List<Long> dbParamIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dbParamList))
        {
            dbParamIdList = dbParamList.stream().map(TagParam::getId).collect(Collectors.toList());
        }

        List<TagParamDto> insertParamList = new ArrayList<>();
        List<TagParamDto> updateParamList = new ArrayList<>();
        List<TagParamDto> deleteParamList = new ArrayList<>();

        // 筛选新增和修改的 标签参数 和 标签参数规则
        if (CollectionUtils.isNotEmpty(frontParamDtoList))
        {
            for (TagParamDto dto : frontParamDtoList)
            {
                dto.setTagId(String.valueOf(tagId));
                // 标签参数ID为空，则新增
                if (dto.getId() == null)
                {
                    dto.setCreator(vo.getModifier());
                    dto.setCreateTime(vo.getModifyTime());
                    insertParamList.add(dto);
                }else
                {
                    if (CollectionUtils.isNotEmpty(dbParamIdList))
                    {
                        Assert.isTrue(dbParamIdList.remove(dto.getId()), "异常标签参数ID");
                    }
                    dto.setModifier(vo.getModifier());
                    dto.setModifyTime(vo.getModifyTime());
                    updateParamList.add(dto);
                }
            }
        }
        // 剩余删除 标签参数 和 标签参数规则
        if (CollectionUtils.isNotEmpty(dbParamIdList))
        {
            deleteParamList = dbParamIdList.stream().map(id -> {
                TagParamDto dto = new TagParamDto();
                dto.setId(id);
                dto.setModifier(vo.getModifier());
                dto.setModifyTime(vo.getModifyTime());
                return dto;
            }).collect(Collectors.toList());
        }

        tagParamService.insertTagParamDtoList(insertParamList);
        tagParamService.updateTagParamDtoList(updateParamList);
        tagParamService.deleteTagParamDtoList(deleteParamList);

        //记录操作记录
        tagOperationLogService.saveTagOperationLog(entity.getTagCode(), dbRecord.getVersion(),
                vo.getOperateContent(), FlowStatusEnum.MODIFY.getCode(), vo.getModifier(), vo.getModifyTime());

        return new TagDto(entity);
    }

    @Override
    @Transactional(value = "mysqlConfigTransactionManager", rollbackFor = Exception.class)
    public void deprecatedTag(TagDto vo)
    {
        QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_code", vo.getTagCode());
        queryWrapper.and(wrapper -> wrapper.eq("tag_status", FlowStatusEnum.PUBLISH.getCode())
                 .or()
                 .eq("tag_status", FlowStatusEnum.OFFLINE.getCode()));
        List<TagEntity> tagList = tagEntityMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(tagList))
        {
            for (TagEntity entity : tagList)
            {
                // 查询标签参数
                List<TagParam> tagParamList = tagParamService.queryTagParamByTagId(entity.getId());
                List<Long> oldParamIdList = tagParamList.stream().map(TagParam::getId).collect(Collectors.toList());
                // 查询标签参数规则
                List<TagParamRule> tagParamRuleList = tagParamRuleService.queryTagParamRuleByTagId(entity.getId());
                List<Long> oldRuleIdList = tagParamRuleList.stream().map(TagParamRule::getId).collect(Collectors.toList());

                // 过期版本
                TagEntity deprecatedTag = new TagEntity();
                deprecatedTag.setId(entity.getId());
                deprecatedTag.setTagStatus(FlowStatusEnum.DEPRECATED.getCode());
                tagEntityMapper.updateById(deprecatedTag);

                tagParamService.deprecatedBatch(oldParamIdList);
                tagParamRuleService.deprecatedBatch(oldRuleIdList);
            }
        }
    }

    private String generateTagCode()
    {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DateTimeConstant.DATE_TIME_KEY);
        String dateFm = format.format(date);

//        int i = (int) (Math.random() * 900) + 100;
        int i = new SecureRandom().nextInt(900)+100;
        return "TAG-" + dateFm + i;
    }

    private QueryWrapper<TagEntity> fuzzyQuery(TagDto vo)
    {
        String tagName = vo.getTagName();
        String tagCode = vo.getTagCode();
        String tagStatus = vo.getTagStatus();
        Date effectiveDate = vo.getEffectiveDate();
        Date expiryDate = vo.getExpiryDate();
        String autoManual = vo.getTagAutomanual();
        String calculationType = vo.getCalculationType();
        String moduleType = vo.getTagModule();
        String moduleCode = vo.getTagModuleCode();
        String valueType = vo.getTagValueType();

        QueryWrapper<TagEntity> queryCondition = new QueryWrapper<>();
        queryCondition.eq("status", StatusEnum.NORMAL.getCode());

        if (StringUtils.isNotEmpty(vo.getId()))
        {
            queryCondition.eq("id", vo.getId());
        }
        if (StringUtils.isNotEmpty(vo.getTagCatalogId()))
        {
            queryCondition.eq("tag_catalog_id", vo.getTagCatalogId());
        }
        if (StringUtils.isNotEmpty(moduleCode) && StringUtils.isNotEmpty(moduleCode.trim()))
        {
            queryCondition.eq("tag_module_code", moduleCode.trim());
        }
        if (StringUtils.isNotEmpty(tagName) && StringUtils.isNotEmpty(tagName.trim()))
        {
            queryCondition.like("tag_name", tagName.trim());
        }
        if (StringUtils.isNotEmpty(tagCode) && StringUtils.isNotEmpty(tagCode.trim()))
        {
            queryCondition.eq("tag_code", tagCode.trim());
        }
        if (StringUtils.isNotEmpty(autoManual) && StringUtils.isNotEmpty(autoManual.trim()))
        {
            queryCondition.eq("tag_automanual", autoManual.trim());
        }
        if (StringUtils.isNotEmpty(calculationType) && StringUtils.isNotEmpty(calculationType.trim()))
        {
            queryCondition.eq("calculation_type", calculationType.trim());
        }
        if (StringUtils.isNotEmpty(moduleType) && StringUtils.isNotEmpty(moduleType.trim()))
        {
            queryCondition.eq("tag_module", moduleType.trim());
        }
        if (StringUtils.isNotEmpty(valueType) && StringUtils.isNotEmpty(valueType.trim()))
        {
            queryCondition.eq("tag_value_type", valueType.trim());
        }
        if (effectiveDate != null)
        {
            queryCondition.apply("DATE_FORMAT(effective_date,'%Y-%m-%d') >= DATE_FORMAT({0},'%Y-%m-%d')", effectiveDate);
        }
        if (expiryDate != null)
        {
            queryCondition.apply("DATE_FORMAT(expiry_date,'%Y-%m-%d') <= DATE_FORMAT({0},'%Y-%m-%d')", expiryDate);
        }

        // 排除版本已过期的记录
        if (StringUtils.isNotEmpty(tagStatus) && StringUtils.isNotEmpty(tagStatus.trim()))
        {
            queryCondition.eq("tag_status", tagStatus.trim());
        }else
        {
            queryCondition.ne("tag_status", FlowStatusEnum.DEPRECATED.getCode());
        }
        queryCondition.orderByDesc("create_time");

        return queryCondition;
    }

    private void reverseCodeName(TagDto tagDto)
    {
        String tagCatalogName = tagCatalogService.queryAllTagCatalogName(Long.valueOf(tagDto.getTagCatalogId()));
        // 标签目录全路径名
        tagDto.setTagAllCatalogName(tagCatalogName);
        tagDto.setTagStatusName(FlowStatusEnum.getName(tagDto.getTagStatus()));

        if (TagEnum.TagModule.ENTERPRISE.getValue().equals(tagDto.getTagModule()))
        {
            String orgName = organizationService.queryNameByOrgCode(tagDto.getTagModuleCode());
            tagDto.setTagModuleName(orgName);
        }else
        {
            Dictionary dictionary = new Dictionary();
            dictionary.setType("CUST_0007");
            dictionary.setVal(tagDto.getTagModuleCode());

            Dictionary sector = dictionaryService.queryDictionary(dictionary);
            tagDto.setTagModuleName(sector.getValDesc());
        }
    }

    private TagEntity queryTagEntityById(TagDto dto)
    {
        Assert.notNull(dto, "入参不能为空");
        Assert.notNull(dto.getId(), "标签Id不能为空");

        TagEntity entity = tagEntityMapper.selectById(dto.getId());
        Assert.notNull(entity, "该标签记录不存在");
        return entity;
    }
}
