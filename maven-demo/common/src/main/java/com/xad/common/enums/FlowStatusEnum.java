package com.xad.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum FlowStatusEnum
{
    /**
     * 流程状态
     */
    ADD("ADD", "新增"),
    /**
     * 流程状态
     */
    MODIFY("MODIFY", "修改"),
    /**
     * 流程状态
     */
    PUBLISH("PUBLISH", "已发布"),
    /**
     * 流程状态
     */
    OFFLINE("OFFLINE", "停用"),
    /**
     * 流程状态
     */
    ONLINE("ONLINE", "启用"),
    /**
     * 流程状态
     */
    DELETED("DELETED", "删除"),
    /**
     * 流程状态
     */
    DEPRECATED("DEPRECATED", "已归档"), // 即将过期不用，但线上可能仍在使用
    ;

    @Getter
    private final String code;

    @Getter
    private final String name;

    public static String getName(String value)
    {
        AtomicReference<String> colValue = new AtomicReference<>();

        Arrays.stream(FlowStatusEnum.values()).collect(Collectors.toList()).forEach(em -> {
            if (em.getCode().equals(value))
            {
                colValue.set(em.getName());
            }
        });
        return colValue.get();
    }
}
