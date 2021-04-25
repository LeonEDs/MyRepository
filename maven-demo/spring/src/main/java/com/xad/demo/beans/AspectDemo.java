package com.xad.demo.beans;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xad
 * @version 1.0
 * @date 2021/3/18
 */
@Component
@Data
@ToString
public class AspectDemo
{
    @Value("${os.name}")
    private String name;
    private float rs;

    public float div(float a, float b)
    {
        this.rs = a / b;
        return this.rs;
    }
}
