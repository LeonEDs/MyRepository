package com.xad.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumericCheckUtils
{
    private final static Pattern numericPattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");

    private final static Pattern positiveIntegerPattern = Pattern.compile("[0-9]+");

    public static boolean isNumeric(String numeric)
    {
        Matcher isNum = numericPattern.matcher(numeric);
        return isNum.matches();
    }

    public static boolean isPositiveInteger(String numeric)
    {
        Matcher isNum = positiveIntegerPattern.matcher(numeric);
        return isNum.matches();
    }
}
