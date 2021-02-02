package com.xad.server.jobhandler.handler;

import com.xad.server.entity.TagInstance;

import java.util.List;

/**
 * @author xad
 * @version 1.0
 * @date 2021/1/27
 */
public interface ParamHandler<T>
{
    List<TagInstance> handle(T handler);

    RuleHandler getRuleHandler();
}
