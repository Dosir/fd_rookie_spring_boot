package com.fd.rookie.spring.boot.config.exception;

import com.alibaba.fastjson.JSON;
import com.fd.rookie.spring.boot.common.Result;
import com.fd.rookie.spring.boot.common.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义统一异常处理类
 */
public class MyExceptionResolver implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(MyExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        Result result = new Result();
        String message = e.getMessage();
        if (e instanceof BusinessException) {
            result.setCode(ResultCode.FAIL);
            result.setMessage(((BusinessException) e).getMsg());
        } else {
            result.setCode(ResultCode.INTERNAL_SERVER_ERROR);
            result.setMessage(message);
        }
        logger.error(message, e);
        responseResult(httpServletResponse, result);
        return new ModelAndView();
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
