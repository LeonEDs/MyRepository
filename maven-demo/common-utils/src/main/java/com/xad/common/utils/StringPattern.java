package com.xad.common.utils;

import java.util.regex.Pattern;

/**
 * @author xad
 * @version 1.0
 * @date 2021/1/12
 */
public class StringPattern
{
    public static String BLANK = "[\\s\\S]*\\S+[\\s\\S]*";

    public static boolean matchers(String str, String pattern)
    {
        return Pattern.compile(pattern).matcher(str).matches();
    }
}
