//package com.demo.thread;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * 标签打标任务，并行，大任务.
// * 切分更小任务串行执行.
// * 小任务最大容量1k.
// *
// * @version 1.0
// * @author xad
// */
//@Slf4j
//public class ParallelTask extends BaseTask
//{
//    private final List<BaseTask> taskList = new LinkedList<>();
//
//    /**
//     * constructor.
//     */
//    public ParallelTask(Long startId, Long endId, String missionCode, String parentCode)
//    {
//        super(startId, endId, "PARALLEL", missionCode, parentCode);
//    }
//
//    public void splitTask(Long startCustId, Long endCustId, String missionCode)
//    {
//        TagDto tagDto = this.getTagDto();
//
//        String missionCode = this.getMissionCode();
//        long startId = this.getPartStartId();
//        long endId = this.getPartEndId();
//
//        long partLength = endId - startId;
//        long newStart = startId;
//        long newEnd = startId;
//        int serialNum = 1;
//
//        if (partLength >= 0 && endId > 0 && startId > 0)
//        {
//            do
//            {
//                newEnd += (JobHandlerConstants.MAX_SERIAL_TASK_NUM - 1);
//                String taskMissionCode = missionCode + "#" + serialNum++;
//                MarkSerialTask task;
//
//                if (newEnd > endId)
//                {
//                    task = new MarkSerialTask(newStart, endId, taskMissionCode, missionCode, tagDto);
//                    taskList.add(task);
//
//                    break;
//                }else
//                {
//                    task = new MarkSerialTask(newStart, newEnd, taskMissionCode, missionCode, tagDto);
//                    taskList.add(task);
//
//                    newStart = newEnd + 1;
//                    newEnd = newStart;
//                }
//            }while (newEnd <= endId);
//        }
//    }
//
//
//
//    /**
//     * call().
//     */
//    @Override
//    public void run()
//    {
//
//    }
//}
