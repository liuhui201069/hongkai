package com.hongkai.code;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import org.slf4j.MDC;

import static util.Constants.TRACE_ID;

/**
 * @author huiliu
 * @since 2017/8/29
 */
public class Result {
    @Getter
    private boolean success;
    @Getter
    private String message;
    @Getter
    private Object result;
    @Getter
    private String traceId = MDC.get(TRACE_ID);

    public Result(Object result) {
        this.success = Boolean.TRUE;
        this.result = result;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success,Object result, String message) {
        this.success = success;
        this.result = result;
        this.message = message;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
