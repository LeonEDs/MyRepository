package com.xad.server.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SelectMapper
{
    @Select("SELECT t5.cust_id AS cust_id, t5.name AS name, t5.abbr_name AS abbr_name, t5.unit_nature AS unit_nature, t5.cust_code AS cust_code, t5.type AS type, t5.status_cd AS status_cd, t5.create_dt AS create_dt, t5.last_update_dt AS last_update_dt, t5.start_dt AS start_dt, t5.end_dt AS end_dt, t6.cust_id AS cust_id, t6.cust_occupation AS cust_occupation, t6.cust_gender AS cust_gender, t6.cust_birth AS cust_birth, t6.cust_age AS cust_age FROM mtc_gg_sale_customer AS t5 LEFT JOIN mtc_gg_sale_custext AS t6 ON t5.cust_id = t6.cust_id WHERE t5.cust_id >= 353969 AND t5.cust_id <= 354968 ORDER BY t5.cust_id ASC LIMIT 330 OFFSET 0;")
    Cursor<Map<String, Object>> querySql();
}
