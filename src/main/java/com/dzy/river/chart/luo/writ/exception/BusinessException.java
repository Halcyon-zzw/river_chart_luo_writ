package com.dzy.river.chart.luo.writ.exception;

import com.dzy.river.chart.luo.writ.common.ResultCode;

/**
 * 业务异常类
 *
 * @author zhuzhiwei
 * @since 2024-10-28
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException() {
        super(ResultCode.BUSINESS_ERROR);
    }

    public BusinessException(String message) {
        super(ResultCode.BUSINESS_ERROR.getCode(), message);
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode);
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(String message, Throwable cause) {
        super(ResultCode.BUSINESS_ERROR.getCode(), message, cause);
    }

    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}