package com.demo.dubbo.provider;

import com.demo.dubbo.api.IDemoService;
import com.demo.dubbo.dao.QueryMethodDao;
import com.demo.dubbo.entity.QueryResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DemoServiceImpl implements IDemoService
{
    @Autowired
    private QueryMethodDao queryMethodDao;

    @Override
    public List<String> testMethod()
    {
        System.out.println(">>> Invoke Method is success \n\n");
        List<String> list = new ArrayList<>();
        List<QueryResultEntity> queryResult = null;
        try
        {
            queryResult = queryMethodDao.queryDemo("Y330303204991BB");
        }catch (Exception e)
        {
            System.out.println(e);
        }
        if (queryResult != null && queryResult.size() > 0)
        {
            QueryResultEntity entity = queryResult.get(0);
            list.add(entity.getId().toString());
            list.add(entity.getResultStr());
            list.add(entity.getResultDate().toString());
        }
        return list;
    }
}
