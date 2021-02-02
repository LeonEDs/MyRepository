package com.xad.server.dwmapper;

import org.springframework.stereotype.Repository;

/**
 * 客户 Mapper.
 * @version 1.0
 * @author xad
 * @date 2020/12/24 0024
 */
@Repository
public interface CustomerMapper
{
    /**
     * 获取客户Id的最大值.
     */
    Long queryMaxCustId();

    /**
     * 获取客户Id的最小值.
     */
    Long queryMinCustId();
}
