package com.xad.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil
{
    //Fuck!!!!!!!!!!! The regular expression is vulnerable to a denial of service attack (ReDOS)
//    private final static Pattern NUMERIC_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

    private final static Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile("[0-9]+");

    public static boolean isPositiveInteger(String numeric)
    {
        Matcher isNum = POSITIVE_INTEGER_PATTERN.matcher(numeric);
        return isNum.matches();
    }

    public static Double getDoubleVal(String val)
    {
        if (!StringUtils.isNumeric(val))
        {
            return 0d;
        }else
        {
            return Double.valueOf(val);
        }
    }

    public static Long getLongVal(String val)
    {
        if (isPositiveInteger(val))
        {
            return Long.valueOf(val);
        }else
        {
            return 0L;
        }
    }

    public static Long getLongVal(Object val)
    {
        return getLongVal(String.valueOf(val));
    }
}
