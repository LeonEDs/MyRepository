package com.xad.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StatusEnum
{
    /**
     * 0
     */
    NORMAL("0", "正常"),
    /**
     * 1
     */
    DELETED("1", "已删除"),
    /**
     * 1000
     */
    S_NOL("1000", "开启"),
    /**
     * 1100
     */
    S_DEL("1100", "关闭"),
    /**
     * 1010
     */
    S_FB("1010", "已反馈"),
    /**
     * 1020
     */
    S_HD("1020", "已解决"),
    /**
     * 1030
     */
    S_UFB("1030", "未反馈"),
    ;

    @Getter
    private final String code;

    @Getter
    private final String name;
}
