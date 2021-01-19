package com.xad.server.service.impl;

import com.xad.server.mapper.SelectMapper;
import com.xad.server.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService
{
    @Autowired
    SelectMapper selectMapper;

    @Override
    public List<Map<String, Object>> querySQL()
    {
        return selectMapper.querySql();
    }
}
