package com.xad.demo.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/25
 */
@Repository
public interface ExecuteMapper
{
    List<Map<String, Object>> queryXxlJobInfo(@Param("executor") String executor, @Param("name") String jobName);

    int updateXxlJobInfo(@Param("id") String id, @Param("columnName") String columnName, @Param("value") String value);
}
