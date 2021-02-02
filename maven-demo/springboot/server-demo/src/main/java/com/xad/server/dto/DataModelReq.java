package com.xad.server.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;
import java.util.Map;

/**
 * org.roof.data.model.dto
 *
 * @author liht
 * @date 2020/12/8
 */
@Builder
@Data
public class DataModelReq
{

    public final static String FOLDER = "folder";
    public final static String TABLE_DIM = "table_dim";
    public final static String TABLE_MEASURE = "table_measure";
    public final static String FIELD_DIM = "field_dim";
    public final static String FIELD_EXPRESSION = "field_expression";

    @Tolerate
    public DataModelReq() {
    }

    private Long id;
    private String type;
    /**
     * cust_id >= start_cust_id
     * cust_id <= end_cust_id
     */
    private Map<String, Object> params;


    private List<String> slist;

    private Long procId;



}
