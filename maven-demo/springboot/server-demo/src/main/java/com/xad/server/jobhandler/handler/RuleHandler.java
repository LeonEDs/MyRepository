package com.xad.server.jobhandler.handler;

import com.xad.common.utils.NumberUtil;
import com.xad.server.dto.DataModelReq;
import com.xad.server.dto.Result;
import com.xad.server.dto.TagParamRuleDto;
import com.xad.server.jobhandler.JobHandlerConstants;
import com.xad.server.jobhandler.core.BaseTask;
import com.xad.server.service.DataModelCoreRestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 标签参数规则处理类.
 *
 * @author xad
 * @version 1.0
 * @date 2020/12/24 0024
 */
@Slf4j
@Service
public class RuleHandler
{
    private DataModelCoreRestClient indexClient;

    private final RetryTemplate template;

    @Autowired
    public void setIndexClient(DataModelCoreRestClient dataModelCoreRestClient)
    {
        this.indexClient = dataModelCoreRestClient;
    }

    public RuleHandler()
    {
        this.template = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000);
        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);
    }

    /**
     * 标签参数规则执行.
     *
     * @param ruleDtoList 标签参数规则
     * @param task        标签执行任务
     * @return 执行结果
     */
    public Map<Long, Boolean> execute(List<TagParamRuleDto> ruleDtoList, BaseTask task)
    {
        return execute(ruleDtoList, task, null);
    }

    /**
     * 标签参数规则执行.
     *
     * @param ruleDtoList  标签参数规则
     * @param task         标签执行任务
     * @param custValueMap 客户指标值
     * @return 执行结果
     */
    public Map<Long, Boolean> execute(List<TagParamRuleDto> ruleDtoList, BaseTask task, Map<Long, String> custValueMap)
    {
        // 所有并且关系的规则.
        final List<TagParamRuleDto> andRelList = new LinkedList<>();
        // 所有或者关系的规则.
        final List<TagParamRuleDto> orRelList = new LinkedList<>();

        // 指标查询结果，仅用于数值型标签.
        Map<Long, String> idxRst = null;
        // 执行结果
        Map<Long, Boolean> calculateRst = new HashMap<>(JobHandlerConstants.MAX_SERIAL_TASK_NUM);

        // 标签参数规则为空时，该标签参数为无效配置，忽视.
        if (CollectionUtils.isNotEmpty(ruleDtoList))
        {
            ruleDtoList.forEach(ruleDto -> {
                String relOperator = ruleDto.getRuleOperator();

                // 按规则间关系分类
                if ("AND".equals(relOperator))
                {
                    andRelList.add(ruleDto);
                }
                if ("OR".equals(relOperator))
                {
                    orRelList.add(ruleDto);
                }
            });

            // 所有并且关系的指标求交集
            if (CollectionUtils.isNotEmpty(andRelList))
            {
                idxRst = doFilter(calculateRst, andRelList, task);
            }

            // 所有或者关系的指标求并集.
            if (CollectionUtils.isNotEmpty(orRelList))
            {
                idxRst = doFilter(calculateRst, orRelList, task);
            }

            if (MapUtils.isNotEmpty(idxRst) && custValueMap != null)
            {
                custValueMap.putAll(idxRst);
            }
        }

        return calculateRst;
    }

    /**
     * 指标规则过滤.
     *
     * @return 返回最后一次查询指标结果，[custId, value].
     * 用于数值型标签赋值（数值型标签仅一个参数配置，一个参数规则配置）.
     */
    public Map<Long, String> doFilter(Map<Long, Boolean> rstMap, List<TagParamRuleDto> ruleDtoList, BaseTask task)
    {
        Map<Long, String> returnLastIdx = null;
        List<Map<String, Object>> idxList;

        try
        {
            Long startId = task.getPartStartId();
            Long endId = task.getPartEndId();

            for (TagParamRuleDto ruleDto : ruleDtoList)
            {
                String rightValue = ruleDto.getCompareValue();
                String compareOperator = ruleDto.getCompareOperator();
                Long indexId = ruleDto.getIndexId();
                String indexName = ruleDto.getIndexName();
                String indexType = ruleDto.getIndexType();
                String fieldName = ruleDto.getFieldName();
                String relOperator = ruleDto.getRuleOperator();

                // 指标接口.
                Map<String, Object> paramMap = new HashMap<>(3);
                paramMap.put(JobHandlerConstants.IDX_PARAM_CUST_START_ID, startId);
                paramMap.put(JobHandlerConstants.IDX_PARAM_CUST_END_ID, endId);
                paramMap.put(JobHandlerConstants.IDX_PARAM_LIMIT, endId - startId + 1);
                DataModelReq request = new DataModelReq();
                request.setId(indexId);
                request.setType(indexType);
                request.setParams(paramMap);

                AtomicReference<String> errorMsg = new AtomicReference<>();
                Result<List<Map<String, Object>>> idxResult = template.execute(ct -> indexClient.dataModelExec(request)
                        , ctx -> {
                            try
                            {
                                return indexClient.dataModelExec(request);
                            }catch (Exception e)
                            {
                                errorMsg.set(e.getMessage());
                            }
                            return null;
                        });

                if (idxResult == null || idxResult.isFail())
                {
                    throw new RuntimeException(errorMsg.get());
                } else
                {
                    idxList = idxResult.getData();
                }

                Long custId;
                String value;
                boolean res;
                boolean rst;

                if (CollectionUtils.isEmpty(idxList))
                {
                    continue;
                }

                returnLastIdx = new HashMap<>();
                for (Map<String, Object> indicator : idxList)
                {
                    custId = NumberUtil.getLongVal(indicator.get(JobHandlerConstants.IDX_RSP_CUST_KEY));
                    if (custId == 0L)
                    {
                        continue;
                    }
                    value = indicator.get(fieldName) == null ? null : String.valueOf(indicator.get(fieldName));
                    if (StringUtils.isEmpty(value) || "null".equals(value))
                    {
                        continue;
                    }
                    returnLastIdx.put(custId, value);

                    res = compareVal(value, rightValue, compareOperator);

                    if ("AND".equals(relOperator)) // 按规则间关系分类
                    {
                        rst = rstMap.getOrDefault(custId, true);
                        rstMap.put(custId, res && rst);
                    } else if ("OR".equals(relOperator))
                    {
                        rst = rstMap.getOrDefault(custId, false);
                        rstMap.put(custId, res || rst);
                    }
                }
            }
        } catch (Exception e)
        {
            throw e;
        } finally
        {
        }

        return returnLastIdx;
    }

    public static boolean compareVal(String leftStr, String rightStr, String operator) {
        if (StringUtils.isEmpty(operator)) {
            return false;
        } else {
            operator = operator.trim();
            if (StringUtils.isNotEmpty(leftStr)) {
                leftStr = leftStr.trim();
            }
            if (StringUtils.isNotEmpty(rightStr)) {
                rightStr = rightStr.trim();
            }
            if ("=".equals(operator)) {
                return StringUtils.equals(leftStr, rightStr);
            }
            if (">=".equals(operator)) {
                return NumberUtil.getDoubleVal(leftStr) >= NumberUtil.getDoubleVal(rightStr);
            }
            if ("<=".equals(operator)) {
                return NumberUtil.getDoubleVal(leftStr) <= NumberUtil.getDoubleVal(rightStr);
            }
            if (">".equals(operator)) {
                return NumberUtil.getDoubleVal(leftStr) > NumberUtil.getDoubleVal(rightStr);
            }
            if ("<>".equals(operator)) {
                log.info("{}<>{},{}", leftStr, rightStr, !leftStr.equals(rightStr));
                return !leftStr.equals(rightStr);
            }
            if ("<".equals(operator)) {
                return NumberUtil.getDoubleVal(leftStr) < NumberUtil.getDoubleVal(rightStr);
            }
        }
        return false;
    }
}
