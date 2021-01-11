package com.demo.service.impl;

import com.demo.mapper.SelectMapper;
import com.demo.service.DemoService;
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
