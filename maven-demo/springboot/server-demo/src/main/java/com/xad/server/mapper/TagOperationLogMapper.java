package com.xad.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xad.server.entity.TagOperationLog;
import org.springframework.stereotype.Repository;

/**
 * 标签和标签策略操作日志 Mapper.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Repository
public interface TagOperationLogMapper extends BaseMapper<TagOperationLog>
{
}
