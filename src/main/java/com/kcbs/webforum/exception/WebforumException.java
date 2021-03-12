package com.kcbs.webforum.exception;

public class WebforumException extends Exception {

    private final Integer code;
    private final String message;

    public WebforumException(Integer code ,String message){
        this.code = code;
        this.message = message;
    }

    public WebforumException(WebforumExceptionEnum webforumExceptionEnum){
        this.code = webforumExceptionEnum.getCode();
        this.message = webforumExceptionEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
