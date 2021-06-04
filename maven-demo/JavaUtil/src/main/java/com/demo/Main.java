package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.thread.BaseTask;
import com.demo.thread.TaskExecutorService;
import com.xad.common.utils.DateTimeFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.owasp.encoder.Encode;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class Main
{

    public static void main(String[] args) throws ParseException, NoSuchAlgorithmException
    {
        Date da1 = new Date(2021, Calendar.JUNE, 1, 8, 0, 0);
        Date da2 = new Date(2021, Calendar.JUNE, 1, 9, 0, 0);
        Date da3 = new Date(2021, Calendar.JUNE, 1, 10, 0, 0);
        Date da4 = new Date(2021, Calendar.JUNE, 1, 11, 0, 0);
        Date da5 = new Date(2021, Calendar.JUNE, 1, 12, 0, 0);
        Date da6 = new Date(2021, Calendar.JUNE, 1, 13, 0, 0);
        Date da7 = new Date(2021, Calendar.JUNE, 1, 14, 0, 0);
        Date da8 = new Date(2021, Calendar.JUNE, 1, 15, 0, 0);
        Date da9 = new Date(2021, Calendar.JUNE, 1, 16, 0, 0);
        Date da10 = new Date(2021, Calendar.JUNE, 1, 17, 0, 0);
        Date da11 = new Date(2021, Calendar.JUNE, 1, 18, 0, 0);
        Date da12 = new Date(2021, Calendar.JUNE, 1, 19, 0, 0);
        Date da13 = new Date(2021, Calendar.JUNE, 1, 20, 0, 0);

        System.out.println("尖峰时段");
        System.out.println(compareTimeQuantum(da1, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da2, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da3, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da4, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da5, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da6, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da7, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da8, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da9, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da10, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da11, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da12, "19:00-21:00"));
        System.out.println(compareTimeQuantum(da13, "19:00-21:00"));

        System.out.println("高峰时段");
        System.out.println(compareTimeQuantum(da1, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da2, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da3, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da4, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da5, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da6, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da7, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da8, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da9, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da10, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da11, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da12, "8:00-11:00,13:00-19:00,21:00-22:00"));
        System.out.println(compareTimeQuantum(da13, "8:00-11:00,13:00-19:00,21:00-22:00"));

        System.out.println("低谷时段");
        System.out.println(compareTimeQuantum(da1, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da2, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da3, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da4, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da5, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da6, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da7, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da8, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da9, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da10, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da11, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da12, "11:00-13:00,22:00-24:00,00:00-8:00"));
        System.out.println(compareTimeQuantum(da13, "11:00-13:00,22:00-24:00,00:00-8:00"));
    }
    public static boolean compareTimeQuantum(Date timeStamp, String timeQuantum)
    {
        try
        {
            boolean result = false;
            if(timeStamp != null && StringUtils.isNotEmpty(timeQuantum))
            {
                timeStamp = new Date(timeStamp.getTime() + 1800000); //将原时间增加10分钟，避免边界值未被计算问题
                String[] timeQuantumArray = timeQuantum.split(",");
                for (String quantumEntry : timeQuantumArray)
                {
                    if (quantumEntry.indexOf("-") > 0)
                    {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        String[] ts = quantumEntry.split("-");
                        String fs = format.format(timeStamp);
                        Date t = format.parse(fs);
                        Date t1 = format.parse(ts[0]);
                        Date t2 = format.parse(ts[1]);
                        result = result || (t.after(t1) && t.before(t2));
                    }
                }
            }
            return result;
        }catch (Exception e)
        {
            return false;
        }
    }
}
