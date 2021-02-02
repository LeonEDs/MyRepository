package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xad.server.dto.OrganizationDto;
import com.xad.server.entity.Organization;
import com.xad.server.mapper.OrganizationMapper;
import com.xad.server.service.OrganizationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class OrganizationServiceImpl implements OrganizationService
{
    @Autowired
    OrganizationMapper organizationMapper;

    @Override
    public List<OrganizationDto> queryInfoBySector(OrganizationDto vo) {
        Assert.notNull(vo, "入参不能为空");
        // 板块编码
        String sectorCode = vo.getBktype();
        // 上级营运组织编码
        String lastCompCode = vo.getLastComM();
        Organization queryEntity = new Organization();
        if (StringUtils.isNotEmpty(sectorCode)) {
            queryEntity.setBktype(sectorCode);
        }
        if (StringUtils.isNotEmpty(lastCompCode)) {
            queryEntity.setLastComM(lastCompCode);
        }
        queryEntity.setIsbk("否");
        List<Organization> list = organizationMapper.selectList(new QueryWrapper<>(queryEntity));
        List<OrganizationDto> result = null;
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().map(OrganizationDto::new).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<OrganizationDto> queryInfoFuzzyName(OrganizationDto vo)
    {
        Assert.notNull(vo ,"入参不能为空");

        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(vo.getFullName()) && StringUtils.isNotEmpty(vo.getFullName().trim()))
        {
            queryWrapper.like("full_name", vo.getFullName().trim());
        }

        List<Organization> list = organizationMapper.selectList(queryWrapper);
        List<OrganizationDto> result = null;
        if (CollectionUtils.isNotEmpty(list))
        {
            result = list.stream().map(OrganizationDto::new).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public OrganizationDto queryOneInfoByOrgCode(String orgCode)
    {
        Assert.notNull(orgCode ,"入参不能为空");

        Organization queryEntity = new Organization();
        queryEntity.setComCode(orgCode);

        Organization entity = organizationMapper.selectOne(new QueryWrapper<>(queryEntity));
        if (entity != null)
        {
            return new OrganizationDto(entity);
        }
        return null;
    }

    @Override
    public String queryNameByOrgCode(String orgCode)
    {
        OrganizationDto dto = queryOneInfoByOrgCode(orgCode);
        if (dto != null)
        {
            return dto.getFullName();
        }
        return null;
    }

    @Override
    public String querySectorByEnterprise(String orgCode)
    {
        OrganizationDto dto = queryOneInfoByOrgCode(orgCode);
        if (dto != null)
        {
            return dto.getBktype();
        }
        return null;
    }
}
