package com.xad.server.service;

import com.xad.server.dto.OrganizationDto;

import java.util.List;

/**
 * 组织 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface OrganizationService
{
    /**
     * 查询板块下所有营运组织信息
     */
    List<OrganizationDto> queryInfoBySector(OrganizationDto vo);

    /**
     * 查询营运组织信息
     */
    List<OrganizationDto> queryInfoFuzzyName(OrganizationDto vo);

    /**
     * 查询营运组织信息
     */
    OrganizationDto queryOneInfoByOrgCode(String orgCode);

    /**
     * 查询营运企业名称
     */
    String queryNameByOrgCode(String orgCode);

    /**
     * 查询营运企业所属的板块
     */
    String querySectorByEnterprise(String orgCode);
}
