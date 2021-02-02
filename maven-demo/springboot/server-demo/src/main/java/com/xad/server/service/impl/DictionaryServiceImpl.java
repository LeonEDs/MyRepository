package com.xad.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xad.server.entity.Dictionary;
import com.xad.server.mapper.DictionaryMapper;
import com.xad.server.service.DictionaryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表 Service.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class DictionaryServiceImpl implements DictionaryService
{
    @Autowired
    DictionaryMapper dictionaryMapper;

    @Override
    @Cacheable("Dictionary#queryTagSelectInfo")
    public Map<String, List<Dictionary>> queryTagSelectInfo()
    {
        List<Dictionary> tagModuleList = queryDictionaryByType("TagModule");
        List<Dictionary> autoManualList = queryDictionaryByType("AutoManual");
        List<Dictionary> calculationCycleList = queryDictionaryByType("CalculationCycle");
        List<Dictionary> tagValueTypeList = queryDictionaryByType("TagValueType");

        Map<String, List<Dictionary>> selects = new HashMap<>();

        if (CollectionUtils.isNotEmpty(tagModuleList))
        {
            selects.put("tagModule", tagModuleList);
            selects.put("autoManual", autoManualList);
            selects.put("calculationCycle", calculationCycleList);
            selects.put("tagValueType", tagValueTypeList);
        }
        return selects;
    }
    @Override
    public List<Dictionary> queryDictionaryByType(String type)
    {
        Dictionary condition = new Dictionary();
        condition.setType(type);
        return dictionaryMapper.selectList(new QueryWrapper<>(condition));
    }

    @Override
    public Dictionary queryDictionary(Dictionary vo)
    {
        return dictionaryMapper.selectOne(new QueryWrapper<>(vo));
    }

    @Override
    public Dictionary querySectorGroup()
    {
        Dictionary vo = new Dictionary();
        vo.setType("CUST_0007");
        vo.setSubType("JT");
        return queryDictionary(vo);
    }
}
