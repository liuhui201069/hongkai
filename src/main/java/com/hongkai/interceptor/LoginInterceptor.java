package com.hongkai.interceptor;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hongkai.code.Result;
import com.hongkai.domain.Account;
import com.hongkai.service.AccountService;
import com.hongkai.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.Constants;
import util.DateUtil;

import static util.Constants.SESSION_ID;

/**
 * @author huiliu
 * @date 17/10/22
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    AccountService accountService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    // 应许通过的URL
    private static final String[] IGNORE_URI = {"/", "/login", "/logout"};

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String url = request.getRequestURI().toString();
        boolean flag = false;
        for (String ignoreUri : IGNORE_URI) {
            // 如果是登陆页面的请求 则放过
            if (StringUtils.equals(url, ignoreUri)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            String sessionId = (String)request.getSession().getAttribute(SESSION_ID);
            // 检查是否登陆，否则跳回登陆页面

            if (sessionId == null) {
                PrintWriter out = response.getWriter();
                StringBuilder builder = new StringBuilder();
                if (StringUtils.equals(request.getMethod(), RequestMethod.GET.name())) {
                    log.info("visit without login. {}, {}, {}", url, request.getMethod(), request.getParameterMap());
                    builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                    builder.append("alert(\"您还未登录，请先登录.\");");
                    builder.append("window.top.location.href=\"");
                    builder.append("/\";</script>");
                    out.print(builder.toString());
                } else {
                    log.warn("visit without login. {}, {}, {}", url, request.getMethod(), getBodyData(request));
                    out.print(new Result(false, "请先登录."));
                }
                out.close();
                return false;
            }else{
                //根据session查询用户信息
                Result result = accountService.getAccountBySessionId(sessionId);
                if(!result.isSuccess()){
                    PrintWriter out = response.getWriter();
                    StringBuilder builder = new StringBuilder();
                    builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                    builder.append("alert(\"系统问题，登录失败.\");");
                    builder.append("window.top.location.href=\"");
                    builder.append("/\";</script>");
                    out.print(builder.toString());
                    out.close();
                    return false;
                }else{
                    Account account = (Account)result.getResult();
                    if(account == null){
                        PrintWriter out = response.getWriter();
                        StringBuilder builder = new StringBuilder();
                        builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                        builder.append("alert(\"登录信息过期，请重新登录.\");");
                        builder.append("window.top.location.href=\"");
                        builder.append("/\";</script>");
                        out.print(builder.toString());
                        out.close();
                        return false;
                    }else{
                        if(account.getDisable() == 1){
                            PrintWriter out = response.getWriter();
                            StringBuilder builder = new StringBuilder();
                            builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
                            builder.append("alert(\"账号已被禁用.\");");
                            builder.append("window.top.location.href=\"");
                            builder.append("/\";</script>");
                            out.print(builder.toString());
                            out.close();
                            return false;
                        }else{
                            MDC.put(Constants.USER_ID, account.getUsername());
                            MDC.put(Constants.ACCOUNT_ID, String.valueOf(account.getId()));
                            return true;
                        }
                    }
                }

            }
        }
        return true;
    }

    /**
     * 获取请求体中的字符串(POST)
     */
    private static String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine())) {
                data.append(line);
            }
        } catch (Exception e) {
        } finally {
        }
        return data.toString();
    }
}