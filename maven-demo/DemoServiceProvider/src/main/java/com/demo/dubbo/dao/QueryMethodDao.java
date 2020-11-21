package com.demo.dubbo.dao;

import com.demo.dubbo.entity.QueryResultEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("queryMethodDao")
public interface QueryMethodDao
{
    //'Y33090324571';
    List<QueryResultEntity> queryDemo(@Param("commonId") String id);

}
