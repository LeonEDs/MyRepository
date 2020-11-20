package com.demo.dubbo.entity;

import java.util.Date;

public class QueryResultEntity
{
    Long id;
    String resultStr;
    Date resultDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }
}
