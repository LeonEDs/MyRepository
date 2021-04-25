package com.xad.server.dwmapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/22
 */
@Repository
public interface SelectDwMapper
{
    @Select("SELECT * FROM stf_gg_sale_grpemp LIMIT 1;")
    List<Map<String, Object>> querySql();
}
