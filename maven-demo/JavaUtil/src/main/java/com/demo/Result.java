package com.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.time.ZonedDateTime;

@Data
public class Result<T> {

    public static final String SUCCESSFUL_CODE = "000000";
    public static final String SUCCESSFUL_MESG = "处理成功";

    private String code;
    private String mesg;
    private Instant time;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result() {
        this.time = ZonedDateTime.now().toInstant();
    }


    public Result(String code, String mesg, T data) {
        this.code = code;
        this.mesg = mesg;
        this.data = data;
        this.time = ZonedDateTime.now().toInstant();
    }

}
