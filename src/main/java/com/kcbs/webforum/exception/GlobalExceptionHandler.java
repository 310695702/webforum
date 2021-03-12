package com.kcbs.webforum.exception;

import com.kcbs.webforum.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object handlerException(Exception e, HttpServletResponse response){
        log.error("Default Exception: ",e);
        if (e instanceof NoHandlerFoundException) {
            try {
                response.sendRedirect("/dist/index.html");
            } catch (IOException e1) {
                return ApiRestResponse.error(WebforumExceptionEnum.NOT_FOND);
            }
        }
        if (e instanceof MissingServletRequestParameterException){
            return ApiRestResponse.error(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (e instanceof NumberFormatException){
            return ApiRestResponse.error(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (e instanceof MethodArgumentTypeMismatchException){
            return ApiRestResponse.error(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(WebforumExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object HttpException(HttpRequestMethodNotSupportedException e){
        log.error("HttpRequestMethodNotSupportedException：",e);
        return ApiRestResponse.error(WebforumExceptionEnum.METHOD_ERROR);
    }

    @ExceptionHandler(WebforumException.class)
    public Object handlerWebforumException(WebforumException e){
        log.error("WebforumException: ",e);
        return ApiRestResponse.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handlerMethodException(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException: ", e);
        return  handleBindingResult(e.getBindingResult());
    }

    private ApiRestResponse  handleBindingResult(BindingResult result){
        List<String> list = null;
        if (result.hasErrors()) {
            List<ObjectError> allerrors = result.getAllErrors();
            for (ObjectError obj:allerrors){
                String msg = obj.getDefaultMessage();
                list.add(msg);
            }
        }
        if (list==null){
            return ApiRestResponse.error(WebforumExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse
                .error(WebforumExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }
}
