package com.xad.server.service;

import com.xad.common.constant.ServerNameConstant;
import com.xad.server.dto.DataModelReq;
import com.xad.server.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = ServerNameConstant.ROOF_DATA_MODEL_CORE_SERVICE,url = "http://10.157.93.34/api/data-model-core/v1/")
//@FeignClient(name = ServerNameConstant.ROOF_DATA_MODEL_CORE_SERVICE, path = "/v1")
public interface DataModelCoreRestClient
{

    @PostMapping("/datamodel/exec")
    Result<List<Map<String, Object>>> dataModelExec(@RequestBody DataModelReq req);


    @PostMapping("/datamodel/showSql")
    Result<String> showSql(@RequestBody DataModelReq req);


    @PostMapping("/datamodel/getData")
    Result<List<Map<String, Object>>> getData(@RequestBody Map<String, String> s);


    @PostMapping("/datamodel/execProc")
    Result<String> execProc(@RequestBody Map<String, Object> requestParam);

    @PostMapping("/datamodel/batch")
    Result<String> batch(@RequestBody DataModelReq req);

    @PostMapping("/datamodel/ps")
    Result<String> ps(@RequestBody DataModelReq req);
}
