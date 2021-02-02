package com.demo;

import com.alibaba.fastjson.JSON;
import com.demo.thread.BaseTask;
import com.demo.thread.TaskExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.encoder.Encode;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class Main
{

    public static void main(String[] args)
    {
        Result<String> result = new Result<>("020002", "http://49818.com", null);
        String re = Encode.forHtmlContent(JSON.toJSONString(result));


        System.out.println(re);
    }

}
