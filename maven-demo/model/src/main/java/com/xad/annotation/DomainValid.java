package com.xad.annotation;

import com.xad.consts.CommonPattern;
import com.xad.enums.ValidEnum;
import com.xad.utils.valid.field.ParamValid;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class DomainValid
{
    public static boolean validField(Object rowNum, Object v_data, Map<String, Object> errorData)
    {
        if (errorData == null)
        {
            throw new RuntimeException("The Method validFieldRule parameter errorData can't be null");
        }
        List<Map<String, String>> failedValidField = new LinkedList<Map<String, String>>();
        boolean isFieldValidSuccess = true;

        Class<?> clazz = v_data.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(ParamValid.class)) {
                    try {
                        String fieldName = field.getName();
                        String methodName = "get".concat(fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1)));
                        ParamValid valid = field.getAnnotation(ParamValid.class);
                        Method fieldMethod = clazz.getMethod(methodName);
                        if (fieldMethod != null) {
                            Object value = fieldMethod.invoke(v_data);
                            boolean isFailedValid;
                            // 校验是否为空
                            if (isFailedValid = (valid.notNull()
                                    && (value == null || "".equals(value.toString())))) {
                                Map<String, String> map = new HashMap<String, String>(3);
                                map.put("FieldName", fieldName);
                                map.put("FieldValue", String.valueOf(value));
                                map.put("Rule", "Field Must Not Null");
                                failedValidField.add(map);
                            }
                            // 校验字段长度最大值（字符串，集合容器，数组）
                            if (!isFailedValid && valid.maxLength() > 0 && (value != null
                                    && ((value instanceof CharSequence && ((CharSequence) value).length() > valid.maxLength())
                                    || (value instanceof List && ((Collection) value).size() > valid.maxLength())
                                    || (value instanceof Map && ((Map) value).size() > valid.maxLength())
                                    || (value.getClass().isArray() && arrayLength(value) > valid.maxLength())))) {
                                isFailedValid = true;
                                Map<String, String> map = new HashMap<String, String>(3);
                                map.put("FieldName", fieldName);
                                map.put("FieldValue", String.valueOf(value));
                                map.put("Rule", "Field Max Length: ".concat(String.valueOf(valid.maxLength())));
                                failedValidField.add(map);
                            }
                            // 校验字段长度最小值（字符串，集合容器，数组）
                            if (!isFailedValid && valid.minLength() > 0 && (value != null
                                    && ((value instanceof CharSequence && ((CharSequence) value).length() < valid.minLength())
                                    || (value instanceof List && ((Collection) value).size() < valid.minLength())
                                    || (value instanceof Map && ((Map) value).size() < valid.minLength())
                                    || (value.getClass().isArray() && arrayLength(value) < valid.minLength())))) {
                                isFailedValid = true;
                                Map<String, String> map = new HashMap<String, String>(3);
                                map.put("FieldName", fieldName);
                                map.put("FieldValue", String.valueOf(value));
                                map.put("Rule", "Field Min Length: ".concat(String.valueOf(valid.minLength())));
                                failedValidField.add(map);
                            }
                            // 校验字段值最大值
                            if (!isFailedValid && valid.maxValue() != Integer.MIN_VALUE
                                    && (value != null && !value.toString().matches(CommonPattern.NUMBER_DOUBLE)
                                    || Double.parseDouble(value.toString()) > valid.maxValue())) {
                                isFailedValid = true;
                                Map<String, String> map = new HashMap<String, String>(3);
                                map.put("FieldName", fieldName);
                                map.put("FieldValue", String.valueOf(value));
                                map.put("Rule", "Field Max Value: ".concat(String.valueOf(valid.maxValue())));
                                failedValidField.add(map);
                            }
                            // 校验字段值最小值
                            if (!isFailedValid && valid.minValue() != Integer.MAX_VALUE
                                    && (value != null && !value.toString().matches(CommonPattern.NUMBER_DOUBLE)
                                    || Double.parseDouble(value.toString()) < valid.minValue())) {
                                isFailedValid = true;
                                Map<String, String> map = new HashMap<String, String>(3);
                                map.put("FieldName", fieldName);
                                map.put("FieldValue", String.valueOf(value));
                                map.put("Rule", "Field Min Value: ".concat(String.valueOf(valid.minValue())));
                                failedValidField.add(map);
                            }
                            // 校验字段值正则匹配
                            if (!isFailedValid && !StringUtils.isEmpty(valid.pattern())
                                    && (value != null && !value.toString().matches(valid.pattern()))) {
                                isFailedValid = true;
                                Map<String, String> map = new HashMap<String, String>(3);
                                map.put("FieldName", fieldName);
                                map.put("FieldValue", String.valueOf(value));
                                map.put("Rule", "Field Must Matcher: ".concat(valid.pattern()));
                                failedValidField.add(map);
                            }
                            // 校验字段值是否在枚举中
                            if (!isFailedValid && valid.inEnum() != ValidEnum.EMPTY
                                    && (value != null && !valid.inEnum().isExist(String.valueOf(value)))) {
                                isFailedValid = true;
                                Map<String, String> map = new HashMap<String, String>(3);
                                map.put("FieldName", fieldName);
                                map.put("FieldValue", String.valueOf(value));
                                map.put("Rule", "Field Must In Enum: ".concat(valid.inEnum().print()));
                                failedValidField.add(map);
                            }

                            if (isFailedValid)
                            {
                                isFieldValidSuccess = false;
                                errorData.put(String.valueOf(rowNum), failedValidField);
                            }
                        }
                    } catch (Exception e) {
                        errorData.put(String.valueOf(rowNum), e.toString());
                        isFieldValidSuccess = false;
                    }
                }
            }
        }

        return isFieldValidSuccess;
    }

    private static int arrayLength(Object obj)
    {
        int length = 0;
        if (obj.getClass().isArray())
        {
            if (obj.getClass().getComponentType().isPrimitive())
            {
                Class<?> elementType = obj.getClass().getComponentType();
                if (elementType == short.class)
                {
                    length = ((short [])obj).length;
                }else if (elementType == int.class)
                {
                    length = ((int [])obj).length;
                }else if (elementType == long.class)
                {
                    length = ((long [])obj).length;
                }else if (elementType == char.class)
                {
                    length = ((char [])obj).length;
                }else if (elementType == byte.class)
                {
                    length = ((byte [])obj).length;
                }else if (elementType == float.class)
                {
                    length = ((float [])obj).length;
                }else if (elementType == double.class)
                {
                    length = ((double [])obj).length;
                }else if (elementType == boolean.class)
                {
                    length = ((boolean [])obj).length;
                }
            }else
            {
                length = ((Object []) obj).length;
            }
        }
        return length;
    }
}

