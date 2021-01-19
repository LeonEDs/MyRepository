package com.xad.common.utils;

import static com.xad.common.utils.StringPattern.BLANK;

/**
 * @author xad
 * @version 1.0
 * @date 2021/1/12
 */
public class StringUtils
{
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str)
    {
        return str == null || StringPattern.matchers(str, BLANK);
    }
}
