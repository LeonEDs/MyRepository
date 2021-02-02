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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 无值型标签执行类.
 *
 * 标签配置一条标签参数.
 * 标签参数配置多条标签参数规则.
 * 标签实例值空.
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Service
public class NoValueParamHandler implements ParamHandler<TagMarkTask>
{
    @Override
    public List<TagInstance> handle(TagMarkTask task)
    {
        TagDto tagDto = task.getTagDto();
        String missionCode = task.getMissionCode();

        List<TagInstance> instanceList = new ArrayList<>(JobHandlerConstants.MAX_SERIAL_TASK_NUM);
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
                            TagInstance instance = new TagInstance();
                            instance.setCustId(custId);
                            instance.setTagId(Long.valueOf(tagDto.getId()));
                            instance.setTagCode(tagDto.getTagCode());
                            instance.setExecutionCode(missionCode);
                            instance.setCreateTime(new Date());
                            instance.setTagValue(null);

                            instanceList.add(instance);
                        }
                    });
                }
            }
        }

        return instanceList;
    }

    @Override
    public RuleHandler getRuleHandler()
    {
        return ContextUtils.getBean(RuleHandler.class);
    }
}
