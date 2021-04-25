import com.alibaba.fastjson.JSONObject;
import com.xad.server.MainApplication;
import com.xad.server.dto.DataModelReq;
import com.xad.server.dto.Result;
import com.xad.server.dwmapper.SelectDwMapper;
import com.xad.server.jobhandler.JobHandlerConstants;
import com.xad.server.jobhandler.executor.TagTaskExecutor;
import com.xad.server.service.DataModelCoreRestClient;
import com.xad.server.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MainApplication.class)
public class JUnitTest
{
    @Autowired
    DemoService service;
    @Autowired
    TagTaskExecutor tagTaskExecutor;
    @Autowired
    DataModelCoreRestClient indexClient;
    @Autowired
    SelectDwMapper selectDwMapper;

    @Test
    public void execSQL()
    {
        List<Map<String, Object>> res = selectDwMapper.querySql();
        System.out.println(res.get(0));
    }

    @Test
    public void putTaskId() throws InterruptedException
    {
        Long tagId = 1353562843185680385L;
        tagTaskExecutor.execute(tagId);
        boolean first = true;
        while (!tagTaskExecutor.isFinish())
        {
            if (first)
            {
                first = false;
                System.out.println(">>>>>> START");
            }
        }
        System.out.println(">>>>>> END");
    }

    @Test
    public void dataExec()
    {
        DataModelReq request = new DataModelReq();
        Map<String, Object> paramMap = new HashMap<>(3);
        Long startId = 1320101L;
        Long endId = 1321100L;
        paramMap.put(JobHandlerConstants.IDX_PARAM_CUST_START_ID, startId);
        paramMap.put(JobHandlerConstants.IDX_PARAM_CUST_END_ID, endId);
        paramMap.put(JobHandlerConstants.IDX_PARAM_LIMIT, endId - startId + 1);
        request.setId(25L);
        request.setType("table_measure");
        request.setParams(paramMap);

        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3);
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000);
        template.setRetryPolicy(retryPolicy);
        template.setBackOffPolicy(backOffPolicy);

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
            System.out.println(errorMsg.get());
        } else
        {
            System.out.println(JSONObject.toJSONString(idxResult.getData()));
        }

    }
}
