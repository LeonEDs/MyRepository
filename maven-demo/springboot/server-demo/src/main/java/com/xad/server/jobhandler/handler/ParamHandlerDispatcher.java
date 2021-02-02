package com.xad.server.jobhandler.handler;

import com.xad.common.enums.TagEnum;
import com.xad.server.config.ContextUtils;
import com.xad.server.jobhandler.core.TagMarkTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 标签参数处理类.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class ParamHandlerDispatcher
{
    /**
     * 执行标签任务.
     */
    public ParamHandler<TagMarkTask> dispatch(String tagValueType )
    {
        if (StringUtils.isNotEmpty(tagValueType))
        {
            if (TagEnum.TagValueType.ENUM.getValue().equals(tagValueType))
            {
                return ContextUtils.getBean(EnumParamHandler.class);
            }

            if (TagEnum.TagValueType.NUMBER.getValue().equals(tagValueType))
            {
                return ContextUtils.getBean(NumberParamHandler.class);
            }

            if (TagEnum.TagValueType.NULL.getValue().equals(tagValueType))
            {
                return ContextUtils.getBean(NoValueParamHandler.class);
            }
        }

        return null;
    }
}
