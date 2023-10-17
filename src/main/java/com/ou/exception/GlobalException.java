package com.ou.exception;

import com.ou.common.BaseResponse;
import com.ou.common.ErrorCode;
import com.ou.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常通知类
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException e){
        return ResultUtils.error(e.getCode(),e.getMessage(), e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse businessException(RuntimeException e){
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
