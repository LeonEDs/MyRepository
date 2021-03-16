package com.demo;

import com.alibaba.fastjson.JSON;
import com.demo.thread.BaseTask;
import com.demo.thread.TaskExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.owasp.encoder.Encode;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class Main
{

    public static void main(String[] args) throws ParseException
    {
        String country = "CN, 中国";
        country = country.replaceAll("冻结", "");
        int idx;
        if (StringUtils.isNotEmpty(country) && (idx = country.indexOf(", ")) != -1)
        {
            country = country.substring(0, idx);
        }
        System.out.println(country);
    }

}
