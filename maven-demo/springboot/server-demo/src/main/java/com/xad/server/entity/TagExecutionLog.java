package com.xad.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xad.common.constant.DateTimeConstant;
import com.xad.server.dto.TagExecutionLogDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * 数据实体 - 标签执行日志.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tag_execution_logs")
@NoArgsConstructor
public class TagExecutionLog extends BaseEntity
{
    /** 任务编号 */
    private String executionCode;

    /** 父任务编号 */
    private String parentCode;

    /** 任务类型 */
    private String executionType;

    /** 标签ID */
    private Long tagId;

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

    public TagExecutionLog(TagExecutionLogDto dto)
    {
        super(dto);
        this.executionCode = dto.getExecutionCode();
        this.parentCode = dto.getParentCode();
        this.executionType = dto.getExecutionType();
        this.tagId = StringUtils.isEmpty(dto.getTagId()) ? null : Long.valueOf(dto.getTagId());
        this.tagCode = dto.getTagCode();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.executionTime = dto.getExecutionTime();
        this.executionResult = dto.getExecutionResult();
        this.custIdStart = dto.getCustIdStart();
        this.custIdEnd = dto.getCustIdEnd();
        this.executionSuccess = dto.getExecutionSuccess();
        this.errorMessage = dto.getErrorMessage();
    }
}
