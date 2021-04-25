package com.xad.demo.service;

import java.util.List;
import java.util.Map;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/26
 */
public interface ExecuteService
{
    List<Map<String, Object>> queryXxlJobInfo(String executorId, String jobName);

    int updateXxlJobInfo(String id, String columnName, String value);
}
