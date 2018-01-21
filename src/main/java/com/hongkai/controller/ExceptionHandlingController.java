package com.hongkai.controller;



import com.hongkai.code.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Exception e) {
        log.error("@handleException : ", e);
        return new Result(Boolean.FALSE, "controller exception").toString();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public String handleMissException(Exception e) {
        return new Result(Boolean.FALSE, String.valueOf("以下参数缺失,请正确传入:\n" + e.getMessage())).toString();
    }
}
