package com.xad.server.jobhandler;

/**
 * 标签任务 常量.
 *
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
public interface JobHandlerConstants
{
    /**
     * 大任务切片小任务最大容量.
     */
    Integer MAX_SERIAL_TASK_NUM = 1000;

    /**
     * 指标响应 客户id Key.
     */
    String IDX_RSP_CUST_KEY = "cust_id";

    /**
     * 指标请求 客户id 起始.
     */
    String IDX_PARAM_CUST_START_ID = "start_cust_id";

    /**
     * 指标请求 客户id 结束.
     */
    String IDX_PARAM_CUST_END_ID = "end_cust_id";

    /**
     * 指标请求 查询条数限制.
     */
    String IDX_PARAM_LIMIT = "limit";
}
