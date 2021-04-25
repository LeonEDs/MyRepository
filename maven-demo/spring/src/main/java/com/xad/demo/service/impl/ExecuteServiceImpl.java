package com.xad.demo.service.impl;

import com.xad.demo.mapper.ExecuteMapper;
import com.xad.demo.service.ExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/26
 */
@Service
public class ExecuteServiceImpl implements ExecuteService
{
    @Autowired
    ExecuteMapper executeMapper;

    @Override
    public List<Map<String, Object>> queryXxlJobInfo(String executorId, String jobName)
    {
        return executeMapper.queryXxlJobInfo(executorId, jobName);
    }

    @Override
    @Transactional
    public int updateXxlJobInfo(String id, String columnName, String value)
    {
        return executeMapper.updateXxlJobInfo(id, columnName, value);
    }
}
