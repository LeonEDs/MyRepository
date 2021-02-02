package com.xad.server.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class RequestPage<T>
{
    private T vo;

    private Page<?> page;
}
