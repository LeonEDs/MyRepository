package com.xad.server.service.impl;

import com.xad.server.mapper.SelectMapper;
import com.xad.server.service.DemoService;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService
{
    @Autowired
    SelectMapper selectMapper;

    @Autowired
    @Qualifier("mysqlConfigSqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public List<Map<String, Object>> querySQL()
    {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Cursor<Map<String, Object>> dbResult = sqlSession.getMapper(SelectMapper.class).querySql();
        final List<Map<String, Object>> result = new LinkedList<>();
        System.out.println(dbResult.isOpen());
        dbResult.forEach(rs ->{
            System.out.println(rs.get("name"));
            result.add(rs);
        });
        return result;
    }
}
