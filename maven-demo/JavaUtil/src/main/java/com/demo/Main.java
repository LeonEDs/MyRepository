package com.demo;

import com.demo.clazz.ReflectionUtil;
import com.demo.thread.BaseTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
public class Main {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 开始时间后缀
     */
    private final static String TIME_START_SUFFIX = " 00:00:00";

    /**
     * 结束时间后缀
     */
    private final static String TIME_END_SUFFIX = " 23:59:59";
    private final static DateTimeFormatter FORMATTER_YM = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final String REGEX_OTHER = "[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]";
    private static Map<String, List<Map<String, Object>>> monitorMap = null;
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static Pattern P_NUMBER = Pattern.compile("\\d");

    public static void main(String[] args) throws ParseException, NoSuchAlgorithmException, IOException, ClassNotFoundException {
        List<Class<? extends BaseTask>> line = ReflectionUtil.findClassesInPackage("com.demo.entity", BaseTask.class);
        for (Class l : line){
            System.out.println(l.getName());
        }
    }




}