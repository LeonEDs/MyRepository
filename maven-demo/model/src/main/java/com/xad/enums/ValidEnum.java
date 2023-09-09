package com.xad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

public enum ValidEnum {
    EMPTY(Type.Empty.class),
    ENTER_REASON(Type.EnterReason.class),
    ;

    private Type[] elements;

    ValidEnum(Class<? extends Type> clazz)
    {
        this.elements = clazz.getEnumConstants();
    }

    public boolean isExist(final String code) {
        return Arrays.stream(this.elements).anyMatch(t -> t.getCode().equals(code));
    }

    public String print()
    {
        final StringBuilder builder = new StringBuilder(this.name());
        builder.append(" || {");
        Arrays.stream(this.elements).forEach(t ->
                builder.append("[").append(t.getCode()).append(", ").append(t.getName()).append("], ")
        );
        return builder.toString().substring(0, builder.lastIndexOf(","));
    }

    interface Type {
        String getCode();
        String getName();

        @Getter
        @AllArgsConstructor
        enum Empty implements Type{
            ;

            private String code;

            private String name;
        }

        @Getter
        @AllArgsConstructor
        enum EnterReason implements Type {
            CX("CX", "传讯"),
            TA("TA", "投案"),
            ZACH("ZACH", "治安传唤"),
            JXPW("JXPW", "继续盘问"),
            XSCH("XSCH", "刑事传唤"),
            JC("JC", "拘传"),
            XSJL("XSJL", "刑事拘留"),
            QBHS("QBHS", "取保侯审"),
            JSJZ("JSJZ", "监视居住"),
            DB("DB", "逮捕"),
            BHR("BHR", "被害人"),
            ZR("ZR", "证人"),
            JB("JB", "举报"),
            QZNS("QZNS", "群众扭送"),
            YS("YS", "移送"),
            XCZH("XCZH", "现场抓获"),
            QT("QT", "其他"),
            ;

            private String code;

            private String name;
        }
    }
}
