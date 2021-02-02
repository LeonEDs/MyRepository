package com.xad.server.jobhandler.handler;

import com.xad.server.config.ContextUtils;
import com.xad.server.dto.TagDto;
import com.xad.server.dto.TagParamDto;
import com.xad.server.dto.TagParamRuleDto;
import com.xad.server.entity.TagInstance;
import com.xad.server.jobhandler.JobHandlerConstants;
import com.xad.server.jobhandler.core.TagMarkTask;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 枚举型标签执行类.
 *
 * 标签配置多条标签参数.
 * 标签参数配置多条标签参数规则.
 * 标签实例值为符合条件的枚举值拼接 "1;2;3;4;".
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class EnumParamHandler implements ParamHandler<TagMarkTask>
{
    @Override
    public List<TagInstance> handle(TagMarkTask task)
    {
        TagDto tagDto = task.getTagDto();
        String missionCode = task.getMissionCode();

        final Map<Long, TagInstance> tagInstanceMap = new HashMap<>(JobHandlerConstants.MAX_SERIAL_TASK_NUM);
        List<TagParamDto> paramDtoList = tagDto.getTagParamDtoList();

        if (CollectionUtils.isNotEmpty(paramDtoList))
        {
            for (TagParamDto paramDto : paramDtoList)
            {
                List<TagParamRuleDto> ruleDtoList = paramDto.getTagParamRuleDtoList();

                if (CollectionUtils.isNotEmpty(ruleDtoList))
                {
                    Map<Long, Boolean> executeResult = getRuleHandler().execute(ruleDtoList, task);

                    executeResult.forEach((custId, ifMark) -> {
                        if (ifMark)
                        {
                            TagInstance instance = tagInstanceMap.get(custId); // 检查该客户的标签实例是否已标记

                            if (instance == null)
                            {
                                instance = new TagInstance();
                                instance.setCustId(custId);
                                instance.setTagId(Long.valueOf(tagDto.getId()));
                                instance.setTagCode(tagDto.getTagCode());
                                instance.setExecutionCode(missionCode);
                                instance.setCreateTime(new Date());
                                instance.setTagValue(paramDto.getParamValue() + ";");
                            }else
                            {
                                instance.setTagValue(instance.getTagValue() + paramDto.getParamValue() + ";");
                            }

                            tagInstanceMap.put(custId, instance);
                        }
                    });
                }
            }
        }

        if (!tagInstanceMap.isEmpty())
        {
            return new ArrayList<>(tagInstanceMap.values());
        }
        return null;
    }

    @Override
    public RuleHandler getRuleHandler()
    {
        return ContextUtils.getBean(RuleHandler.class);
    }
}
