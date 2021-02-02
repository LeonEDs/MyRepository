//package com.demo.thread;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author xad
// * @version 1.0
// * @date 2021/1/19
// */
//public class Split
//{
//    private int MAX_PARALLEL_TASK_NUM = 10;
//
//    public void split()
//    {
//        TagDto tagDto = this.getTagDto();
//
//        List<BaseTask> markTaskList = new ArrayList<>(MAX_PARALLEL_TASK_NUM);
//
//        Long maxCustId = tagInstanceService.getMaxCustId();
//        Long minCustId = tagInstanceService.getMinCustId();
//        String missionCode = generateMissionCode();
//
//        if (maxCustId != null && minCustId != null && maxCustId > 0 && minCustId > 0)
//        {
//            logDto.setTagId(tagDto.getId());
//            logDto.setTagCode(tagDto.getTagCode());
//            logDto.setExecutionCode(missionCode);
//            logDto.setExecutionType(ExecutionEnum.BaseTask.getCode());
//            logDto.setCustIdStart(minCustId);
//            logDto.setCustIdEnd(maxCustId);
//            logDto.setStartTime(new Date());
//            TagJobFactory.newTagServiceInvoker().executionLog(logDto);
//
//            int parallelNum = 1;
//            long partLength = maxCustId - minCustId;
//
//            if (partLength <= 1000)
//            {
//                String taskMissionCode = missionCode + "#1";
//                ParallelTask task = new ParallelTask(minCustId, maxCustId, taskMissionCode, missionCode, tagDto);
//
//                markTaskList.add(task);
//            }else
//            {
//                long split = partLength / MAX_PARALLEL_TASK_NUM;
//                for (int i = 0; i < MAX_PARALLEL_TASK_NUM; i++)
//                {
//                    ParallelTask task;
//                    long startId;
//                    long endId;
//                    String taskMissionCode = missionCode + "#" + parallelNum++;
//
//                    if (i == (MAX_PARALLEL_TASK_NUM - 1))
//                    {
//                        startId = minCustId + split*i;
//                        endId = maxCustId;
//                    }else
//                    {
//                        startId = minCustId + split*i;
//                        endId = minCustId + split*(i+1) - 1;
//                    }
//
//                    task = new ParallelTask(startId, endId, taskMissionCode, missionCode, tagDto);
//                    markTaskList.add(task);
//                }
//            }
//        }
//
//        return markTaskList;
//    }
//}
