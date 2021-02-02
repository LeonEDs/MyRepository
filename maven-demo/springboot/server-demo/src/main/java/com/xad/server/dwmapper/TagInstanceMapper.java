package com.xad.server.dwmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xad.server.entity.TagInstance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 标签实例 Mapper.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Repository
public interface TagInstanceMapper extends BaseMapper<TagInstance>
{
    /**
     * 标签实例 新增.
     * @param tagInstance 标签实例.
     * @return 结果.
     */
    int insertWithSEQ(@Param("inst") TagInstance tagInstance);
}
