package com.dzy.river.chart.luo.writ.exception;

import com.dzy.river.chart.luo.writ.common.ResultCode;

/**
 * 数据不存在异常
 *
 * @author zhuzhiwei
 * @since 2024-10-28
 */
public class DataNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public DataNotFoundException() {
        super(ResultCode.DATA_NOT_FOUND);
    }

    public DataNotFoundException(String message) {
        super(ResultCode.DATA_NOT_FOUND.getCode(), message);
    }

    public DataNotFoundException(String resourceName, Object resourceId) {
        super(ResultCode.DATA_NOT_FOUND.getCode(),
              String.format("%s不存在，ID: %s", resourceName, resourceId));
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(ResultCode.DATA_NOT_FOUND.getCode(), message, cause);
    }
}