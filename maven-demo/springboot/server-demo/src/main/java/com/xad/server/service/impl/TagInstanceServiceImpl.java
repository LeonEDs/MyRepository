package com.xad.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xad.common.enums.FlowStatusEnum;
import com.xad.common.enums.StatusEnum;
import com.xad.server.dto.*;
import com.xad.server.dto.TagInstanceDto;
import com.xad.server.dto.TagInstanceLogDto;
import com.xad.server.dwmapper.CustomerMapper;
import com.xad.server.dwmapper.TagInstanceMapper;
import com.xad.server.entity.TagCatalog;
import com.xad.server.entity.TagEntity;
import com.xad.server.entity.TagInstance;
import com.xad.server.entity.TagParam;
import com.xad.server.mapper.TagCatalogMapper;
import com.xad.server.mapper.TagEntityMapper;
import com.xad.server.service.TagInstanceService;
import com.xad.server.service.TagParamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 标签实例 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
@Slf4j
public class TagInstanceServiceImpl implements TagInstanceService {
    @Autowired
    private TagInstanceMapper tagInstanceMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    TagEntityMapper tagEntityMapper;

    @Autowired
    TagCatalogMapper tagCatalogMapper;

    @Autowired
    RestHighLevelClient highLevelClient;

    @Autowired
    TagParamService tagParamService;

    @Override
    public List<TagInstanceDto> queryTagInstanceByCustId(Long custId) {
        Assert.notNull(custId, "客户ID不能为空");
        List<TagInstanceDto> instanceDtoList = new ArrayList<>();
        TagInstance entity = new TagInstance();
        entity.setStatus(StatusEnum.NORMAL.getCode());
        entity.setCustId(custId);
        List<TagInstance> instanceList = tagInstanceMapper.selectList(new QueryWrapper<>(entity));
        //TagInstance添加名称
        List<Long> tagIds = instanceList.stream().map(TagInstance::getTagId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tagIds)) {
            return instanceDtoList;
        }
        List<TagEntity> tagEntityList = tagEntityMapper.selectList(new QueryWrapper<TagEntity>().in("id", tagIds));
        if (CollectionUtils.isNotEmpty(tagEntityList)) {
            Map<Long, String> tagIdNameMap = tagEntityList.stream().collect(Collectors.toMap(TagEntity::getId, TagEntity::getTagName));
            Map<Long, String> tagIdAutoMap = tagEntityList.stream().collect(Collectors.toMap(TagEntity::getId, TagEntity::getTagAutomanual));
            instanceList.forEach(instance -> instance.setTagName(tagIdNameMap.get(instance.getTagId())));
            instanceList.forEach(instance -> instance.setTagAutoType(tagIdAutoMap.get(instance.getTagId())));

            instanceList.forEach(tagInstance -> {
                TagInstanceDto dto = new TagInstanceDto();
                dto.setId(tagInstance.getId());
                dto.setTagId(String.valueOf(tagInstance.getTagId()));
                dto.setTagName(tagInstance.getTagName());
                dto.setTagValue(tagInstance.getTagValue());
                dto.setTagCode(tagInstance.getTagCode());
                dto.setTagAutoType(tagInstance.getTagAutoType());
                dto.setCustId(tagInstance.getCustId());
                dto.setCreateTime(tagInstance.getCreateTime());
                dto.setCustId(tagInstance.getCustId());
                dto.setCreator(tagInstance.getCreator());
                TagEntity tagEntity = tagEntityMapper.selectById(tagInstance.getTagId());
                if(tagEntity != null){
                    dto.setTagValueType(tagEntity.getTagValueType());
                    List<TagParam> tagParamList = tagParamService.queryTagParamByTagId(tagInstance.getTagId());
                    if (CollectionUtils.isNotEmpty(tagParamList)) {
                        List<TagParamDto> tagParamDtoList = tagParamList.stream().map(TagParamDto::new).collect(Collectors.toList());
                        dto.setTagParamDtoList(tagParamDtoList);
                    }
                }
                instanceDtoList.add(dto);
            });
        }
        return instanceDtoList;
    }

    @Override
    @Transactional(value = "dwConfigTransactionManager", rollbackFor = Exception.class)
    public int deleteBatchByTagCode(String tagCode) {
        Assert.notNull("标签编码tagCode不能为空");
        Map<String, Object> columnMap = new HashMap<>(1);
        columnMap.put("tag_code", tagCode);
        return tagInstanceMapper.deleteByMap(columnMap);
    }

    @Override
    @Transactional(value = "dwConfigTransactionManager", rollbackFor = Exception.class)
    public int deleteByTagCodeWithCustId(String tagCode, Long custId) {
        Assert.notNull("标签编码tagCode不能为空");
        Map<String, Object> columnMap = new HashMap<>(2);
        columnMap.put("tag_code", tagCode);
        columnMap.put("cust_id", custId);
        return tagInstanceMapper.deleteByMap(columnMap);
    }

    @Override
    @Transactional(value = "dwConfigTransactionManager", rollbackFor = Exception.class)
    public int deleteByTagCodeWithCustId(String tagCode, Long startCustId, Long endCustId) {
        Assert.notNull("标签编码tagCode不能为空");
        QueryWrapper<TagInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_code", tagCode);
        queryWrapper.ge("cust_id", startCustId);
        queryWrapper.le("cust_id", endCustId);
        return tagInstanceMapper.delete(queryWrapper);
    }

    @Override
    @Transactional(value = "dwConfigTransactionManager", rollbackFor = Exception.class)
    public void insertBatch(List<TagInstance> tagInstanceList) {
        if (CollectionUtils.isNotEmpty(tagInstanceList)) {
            tagInstanceList.forEach(tagInstance -> {
                tagInstance.setId(null);
                tagInstanceMapper.insertWithSEQ(tagInstance);
            });
        }
    }

    @Override
    public int updateBatchByTagCode(TagInstance tagInstance, String tagCode) {
        QueryWrapper<TagInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_code", tagCode);
        return tagInstanceMapper.update(tagInstance, queryWrapper);
    }

    @Override
    @Transactional(value = "dwConfigTransactionManager", rollbackFor = Exception.class)
    public String removeTagInstance(Long id) {
        TagInstance tagInstance = tagInstanceMapper.selectById(id);
        tagInstance.setStatus("1");
        int update = tagInstanceMapper.updateById(tagInstance);
        return update > 0 ? "success" : "failed";
    }


    @Override
    public Long getMaxCustId() {
        return customerMapper.queryMaxCustId();
    }

    @Override
    public Long getMinCustId() {
        return customerMapper.queryMinCustId();
    }
}
