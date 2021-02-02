package com.xad.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 执行任务枚举
 */
@AllArgsConstructor
public enum ExecutionEnum
{
    /**
     * 结果
     */
    SUCCESS("SUCCESS", "成功"),
    /**
     * 结果
     */
    FAILED("FAILED", "失败"),
    /**
     * 任务类型
     */
    MarkTask("MarkTask", "打标任务"),
    /**
     * 任务类型
     */
    SerialTask("SerialTask", "切片串行小任务"),
    ;

    @Getter
    private final String code;

    @Getter
    private final String name;

    public static String getName(String value)
    {
        AtomicReference<String> colValue = new AtomicReference<>();

        Arrays.stream(ExecutionEnum.values()).collect(Collectors.toList()).forEach(em -> {
            if (em.getCode().equals(value))
            {
                colValue.set(em.getName());
            }
        });
        return colValue.get();
    }
}
