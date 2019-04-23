package com.fd.rookie.spring.boot.config.exception;

import com.fd.rookie.spring.boot.common.Result;
import com.fd.rookie.spring.boot.common.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 通过注解的方式进行  全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 对捕获的异常进行处理并打印日志等，之后返回json数据，方式与Controller相同
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result businessExceptionHandler(BusinessException e) {
        Result result = new Result();
        result.setCode(ResultCode.FAIL);
        result.setMessage(e.getMsg());
        return result;
    }

    /**
     * 当然也可以直接返回ModelAndView等类型，然后跳转相应的错误页面，这都根据实际的需要进行使用
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView exceptionHandler(Exception e) {
        return new ModelAndView();
    }
}
