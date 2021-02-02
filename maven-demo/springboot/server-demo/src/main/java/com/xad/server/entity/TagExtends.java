package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tag_extends.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@TableName(value = "tag_extends")
@NoArgsConstructor
@AllArgsConstructor
public class TagExtends {

    private Integer id;

    /**
     * 标签编码.
     */
    private String tagCode;

    /**
     * 标签查询次数.
     */
    private Long searchCount;

    /**
     * 标签ID，（考虑需不需要）.
     */
    private Long tagId;

    private Integer status;

    private static final long serialVersionUID = 1L;
}