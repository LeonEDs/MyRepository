package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.thread.BaseTask;
import com.demo.thread.TaskExecutorService;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class Main
{

    public static void main(String[] args) throws ParseException, NoSuchAlgorithmException
    {
        String pattern = "abababb";
        int next[] = new int[10];

        int i, j, slen;
        slen = pattern.length();
        i = 0;
        next[0] = -1;
        j = -1;

        while (i < slen)
        {
            if (j == -1 || pattern.charAt(i) == pattern.charAt(j))
            {
                ++i;
                ++j;
                next[i] = j;
            } else
            {
                j = next[j];
            }
        }
        for (int x = 0; x < 10; x++)
        {
            System.out.println(next[x]);
        }
    }

}
