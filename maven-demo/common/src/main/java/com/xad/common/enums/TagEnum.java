package com.xad.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TagEnum
{
    @AllArgsConstructor
    public enum TagModule
    {
        /**
         * 标签等级
         */
        GROUP("GROUP", "集团级", "200101"),
        /**
         * 标签等级
         */
        SECTOR("SECTOR", "板块级", "200102"),
        /**
         * 标签等级
         */
        ENTERPRISE("ENTERPRISE", "营运企业", "200103"),
        ;

        @Getter
        private final String code;

        @Getter
        private final String name;

        @Getter
        private final String value;

        public static String getName(String value)
        {
            AtomicReference<String> colValue = new AtomicReference<>();

            Arrays.stream(TagModule.values()).collect(Collectors.toList()).forEach(em -> {
                if (em.getValue().equals(value))
                {
                    colValue.set(em.getName());
                }
            });
            return colValue.get();
        }
    }

    @AllArgsConstructor
    public enum AutoManual
    {
        /**
         * 标签属性
         */
        AUTO("AUTO", "自动", "200201"),
        /**
         * 标签属性
         */
        MANUAL("MANUAL", "手动", "200202"),
//        AUTO_MANUAL("AUTO_MANUAL", "自动/手动", "200203"),
        ;

        @Getter
        private final String code;

        @Getter
        private final String name;

        @Getter
        private final String value;

        public static String getName(String value)
        {
            AtomicReference<String> colValue = new AtomicReference<>();

            Arrays.stream(AutoManual.values()).collect(Collectors.toList()).forEach(em -> {
                if (em.getValue().equals(value))
                {
                    colValue.set(em.getName());
                }
            });
            return colValue.get();
        }
    }

    @AllArgsConstructor
    public enum CalculationType
    {
//        PROPERTY("PROPERTY", "属性类"),
        /**
         * 标签分类
         */
        RULE("RULE", "规则类"),
//        MODEL("MODEL", "模型类"),
        ;

        @Getter
        private final String code;

        @Getter
        private final String name;
    }

    @AllArgsConstructor
    public enum CalculationCycle
    {
        /**
         * 标签执行周期
         */
        EVERY_DAY("EVERY_DAY", "每日", "200301"),
        /**
         * 标签执行周期
         */
        EVERY_WEEK("EVERY_WEEK", "每周", "200302"),
        /**
         * 标签执行周期
         */
        EVERY_MONTH("EVERY_MONTH", "每月", "200303"),
        /**
         * 标签执行周期
         */
        MANUAL("MANUAL", "手动", "200304"),
        ;

        @Getter
        private final String code;

        @Getter
        private final String name;

        @Getter
        private final String value;

        public static String getName(String value)
        {
            AtomicReference<String> colValue = new AtomicReference<>();

            Arrays.stream(CalculationCycle.values()).collect(Collectors.toList()).forEach(em -> {
                if (em.getValue().equals(value))
                {
                    colValue.set(em.getName());
                }
            });
            return colValue.get();
        }
    }

    @AllArgsConstructor
    public enum TagValueType
    {
        /**
         * 标签值类型
         */
        NUMBER("NUMBER", "数值型", "200401"),
        /**
         * 标签值类型
         */
        ENUM("ENUM", "枚举型", "200402"),
        /**
         * 标签值类型
         */
        NULL("NULL", "无值型", "200403"),
        ;

        @Getter
        private final String code;

        @Getter
        private final String name;

        @Getter
        private final String value;

        public static String getName(String value)
        {
            AtomicReference<String> colValue = new AtomicReference<>();

            Arrays.stream(TagValueType.values()).collect(Collectors.toList()).forEach(em -> {
                if (em.getValue().equals(value))
                {
                    colValue.set(em.getName());
                }
            });
            return colValue.get();
        }
    }

    @AllArgsConstructor
    public enum ObjType
    {
        /**
         * 操作日志类型
         */
        TAG_LOG("TAG_LOG", "标签日志"),
        /**
         * 操作日志类型
         */
        TAG_STRATEGY_LOG("TAG_STRATEGY_LOG", "标签策略日志"),
        ;

        @Getter
        private final String code;

        @Getter
        private final String name;
    }
}
