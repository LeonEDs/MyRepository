package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wucy
 * @Description 字典表entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "dictionary")
public class Dictionary extends BaseEntity{
    /**
     * '数据类型编码'
     */
    private String type;
    /**
     * '数据类型名称'
     */
    private String typeName;
    /**
     * 小类编码
     */
    private String subType;
    /**
     * 小类编码名称
     */
    private String subTypeName;
    /**
     * '值'
     * @return
     */
    private String val;

    /**
     * '值释义'
     */
    private String valDesc;

}
