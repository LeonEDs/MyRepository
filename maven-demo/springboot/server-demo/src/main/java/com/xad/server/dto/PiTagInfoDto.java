package com.xad.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 标签信息 PI.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PiTagInfoDto {
    private Long id;
    private Long custId;
    private Long tagId;
    private String tagCode;
    private String tagValue;
}
