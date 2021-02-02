package com.xad.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.entity.TagExecutionLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 标签执行日志.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TagExecutionLogDto extends BaseDto
{
    private TagDto tagDto;

    private String tagName;

    private String tagModule;

    private String tagModuleCode;

    /** 任务编号 */
    private String executionCode;

    /** 父任务编号 */
    private String parentCode;

    /** 任务类型 */
    private String executionType;

    /** 标签ID */
    private String tagId;

    /** 标签编码 */
    private String tagCode;

    /** 执行开始时间 */
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date startTime;

    /** 执行结束时间 */
    @JsonFormat(pattern = DateTimeConstant.DATE_TIME_PATTERN, timezone = DateTimeConstant.TIME_ZONE_GMT_PLUS_8)
    private Date endTime;

    /** 执行总计时间 */
    private String executionTime;

    /** 执行结果 */
    private String executionResult;

    /** 任务开始客户Id */
    private Long custIdStart;

    /** 任务结束客户Id */
    private Long custIdEnd;

    /** 成功打标数 */
    private Long executionSuccess;

    /** 错误信息 */
    private String errorMessage;

    public TagExecutionLogDto(TagExecutionLog entity)
    {
        super(entity);
        this.executionCode = entity.getExecutionCode();
        this.parentCode = entity.getParentCode();
        this.executionType = entity.getExecutionType();
        this.tagId = entity.getTagId() == null ? null : String.valueOf(entity.getTagId());
        this.tagCode = entity.getTagCode();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.executionTime = entity.getExecutionTime();
        this.executionResult = entity.getExecutionResult();
        this.custIdStart = entity.getCustIdStart();
        this.custIdEnd = entity.getCustIdEnd();
        this.executionSuccess = entity.getExecutionSuccess();
        this.errorMessage = entity.getErrorMessage();
    }
}
