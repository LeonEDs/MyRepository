package MyUtil.DateTimeUtil;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeFormatUtils
{
    /**
     * 时间格式-秒
     */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"; // 小时 HH 表示24小时制 hh 表示12小时制
    /**
     * 时间格式-日
     */
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式-日
     */
    private static final String TIME_ZONE_GMT_PLUS_8 = "GMT+8";

    public static String parseToString(Date dateTime)
    {
        return parseToString(dateTime, DATE_TIME_PATTERN);
    }

    public static String parseToString(Date dateTime, String pattern)
    {
        if (dateTime != null && StringUtils.isNotEmpty(pattern))
        {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(dateTime);
        }
        return null;
    }

    public static String parseToString(LocalDateTime dateTime, String pattern)
    {
        if (dateTime != null && StringUtils.isNotEmpty(pattern))
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
            return format.format(dateTime);
        }
        return null;
    }

    public static String parseToString(LocalDateTime dateTime)
    {
        return parseToString(dateTime, DATE_TIME_PATTERN);
    }

    public static Date parseToDate(String dateTime)
    {
        try
        {
            if (dateTime != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
                return format.parse(dateTime);
            }
        } catch (Exception ignore)
        {
        }
        return null;
    }

    public static LocalDateTime parseToLocalDateTime(String dateTime)
    {
        try
        {
            if (dateTime != null)
            {
                DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
                return LocalDateTime.parse(dateTime, df);
            }
        } catch (Exception ignore)
        {
        }
        return null;
    }

    public static LocalDateTime parseToLocalDateTime(Date dateTime)
    {
        try
        {
            if (dateTime != null)
            {
                return LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault());
            }
        } catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static Date parseLocalDateTimeToDate(LocalDateTime dateTime)
    {
        try
        {
            if (dateTime != null)
            {
                return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            }
        } catch (Exception ignore)
        {
        }
        return null;
    }

    /**
     * 获取当前时间 LocalDateTime
     */
    public static LocalDateTime nowLocalDateTime()
    {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

}
