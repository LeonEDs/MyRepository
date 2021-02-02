package com.xad.server.jobhandler;

import com.alibaba.fastjson.JSON;
import com.xad.server.dto.TagExecutionLogDto;
import com.xad.server.entity.TagInstance;
import com.xad.server.jobhandler.handler.ParamHandlerDispatcher;
import com.xad.server.jobhandler.handler.ParamHandler;
import com.xad.server.jobhandler.listener.event.FailedTaskEvent;
import com.xad.server.jobhandler.listener.event.SuccessTaskEvent;
import com.xad.server.service.TagExecutionLogService;
import com.xad.server.service.TagInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务invoker.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Slf4j
@Service
public class ServiceInvoker
{
    private final static String ES_TAG_INSTANCE_IDX = "tag_instance_record";

    private TagInstanceService tagInstanceService;

    private RestHighLevelClient elasticSearchClient;

    private TagExecutionLogService tagExLogService;

    private ParamHandlerDispatcher paramHandlerDispatcher;

    private ApplicationEventPublisher eventPublisher;

    public void insertTagInstanceList(List<TagInstance> list)
    {
        tagInstanceService.insertBatch(list);
    }

    public void logToElastic(List<TagInstance> list)
    {
        logToElastic(ES_TAG_INSTANCE_IDX, list);
    }

    /**
     * ES 保存历史.
     * @param indexName "tag_instance_record"
     */
    public void logToElastic(String indexName, List<TagInstance> list)
    {
        if (CollectionUtils.isNotEmpty(list))
        {
            createIndex(indexName);
            try
            {
                BulkRequest bulkRequest = new BulkRequest();
                IndexRequest request = null;
                for (TagInstance bulk : list)
                {
                    request = new IndexRequest("post");
                    // 自动生成ID
                    request.index(indexName).source(JSON.toJSONString(bulk), XContentType.JSON);
                    bulkRequest.add(request);
                }
                BulkResponse response = elasticSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);

                if (response.hasFailures())
                {
                    throw new RuntimeException("ES批量插入失败");
                }
            } catch (Exception e)
            {
                log.error("ES批量插入失败:", e);
            }
        }
    }

    /**
     * 记录执行日志(新建或修改).
     */
    public void saveOrUpdateExecLog(TagExecutionLogDto dto)
    {
        TagExecutionLogDto result = tagExLogService.saveAndUpdate(dto);
        BeanUtils.copyProperties(result, dto);
    }

    /**
     * ES 检查索引是否存在.
     */
    private boolean existsIndex(String indexName)
    {
        try
        {
            GetIndexRequest request = new GetIndexRequest(indexName);
            request.local(false);
            request.humanReadable(true);
            request.includeDefaults(false);
            request.indicesOptions(IndicesOptions.strictExpandOpen());
            return elasticSearchClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception ignore)
        {
        }
        return false;
    }

    /**
     * ES 创建索引.
     */
    public void createIndex(String indexName)
    {
        try
        {
            if (!existsIndex(indexName))
            {
                CreateIndexRequest request = new CreateIndexRequest(indexName);
                request.settings(Settings.builder()
                        .put("index.number_of_shards", 3)
                        .put("index.number_of_replicas", 2)
                );

                Map<String, Object> properties = new HashMap<>();

                Map<String, Object> fields = new HashMap<>();
                Map<String, Object> keyword = new HashMap<>();
                keyword.put("type", "keyword");
                keyword.put("ignore_above", 256);
                fields.put("keyword", keyword);

                Map<String, Object> tagId = new HashMap<>();
                tagId.put("type", "long");
                properties.put("tagId", tagId);

                Map<String, Object> tagCode = new HashMap<>();
                tagCode.put("type", "text");
                tagCode.put("fields", fields);
                properties.put("tagCode", tagCode);

                Map<String, Object> custId = new HashMap<>();
                custId.put("type", "long");
                properties.put("custId", custId);

                Map<String, Object> tagValue = new HashMap<>();
                tagValue.put("type", "text");
                tagValue.put("fields", fields);
                properties.put("tagValue", tagValue);

                Map<String, Object> executionCode = new HashMap<>();
                executionCode.put("type", "text");
                executionCode.put("fields", fields);
                properties.put("executionCode", executionCode);

                Map<String, Object> remark = new HashMap<>();
                remark.put("type", "text");
                remark.put("fields", fields);
                properties.put("remark", remark);

                Map<String, Object> mapping = new HashMap<>();
                mapping.put("properties", properties);
                request.mapping(mapping);


                request.mapping(mapping);
                CreateIndexResponse res = elasticSearchClient.indices().create(request, RequestOptions.DEFAULT);

                if (!res.isAcknowledged())
                {
                    throw new RuntimeException("创建索引失败" + indexName);
                }
            }
        } catch (Exception ignore) {}
    }

    /**
     * 发起事件.
     */
    public void sendEvent(FailedTaskEvent<TagExecutionLogDto> event)
    {
        eventPublisher.publishEvent(event);
    }

    /**
     * 发起事件.
     */
    public void sendEvent(SuccessTaskEvent<TagExecutionLogDto> event)
    {
        eventPublisher.publishEvent(event);
    }

    @Autowired
    public void setTagInstanceService(TagInstanceService service)
    {
        this.tagInstanceService = service;
    }

    @Autowired
    public void setElasticSearchClient(RestHighLevelClient client)
    {
        this.elasticSearchClient = client;
    }

    @Autowired
    public void setTagExLogService(TagExecutionLogService service)
    {
        this.tagExLogService = service;
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setParamHandlerDispatcher(ParamHandlerDispatcher paramHandlerDispatcher)
    {
        this.paramHandlerDispatcher = paramHandlerDispatcher;
    }

    public ParamHandlerDispatcher getParamHandlerDispatcher()
    {
        return paramHandlerDispatcher;
    }
}
