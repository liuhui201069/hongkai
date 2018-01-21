package com.hongkai.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hongkai.code.Result;
import com.hongkai.domain.Account;
import com.hongkai.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import util.UniqueId;

import static util.Constants.SESSION_ID;

/**
 * @author huiliu
 * @date 17/10/22
 */
@Controller
@Slf4j
@SessionAttributes({SESSION_ID})
public class LoginController {
    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/login")
    public String login(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password", required = false) String password,
                        ModelMap model,
                        HttpServletRequest request) {

        if (request.getMethod() == RequestMethod.GET.name()) {
            return "/";
        }

        log.info("@LoginController@ login.username:{},password:{}", username, password);
        Result result = accountService.auth(username, password);
        if (result.isSuccess()) {
            String sessionid = UniqueId.getUniqeId() + "_hk";
            model.addAttribute(SESSION_ID, sessionid);
            Account account = (Account)result.getResult();
            accountService.updateSessionId(account.getId(), sessionid);
            return "/index";
        } else {
            model.put("tips", result.getMessage());
            return "/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(SessionStatus sessionStatus,
                       ModelMap modelMap,
                       HttpServletResponse response) {
        log.info("@LoginController@ logout. {}", modelMap.get(SESSION_ID));
        sessionStatus.setComplete();
        java.io.PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println("<html>");
            out.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
            out.append("window.top.location.href=\"");
            out.append("/\";</script>");
            out.println("</html>");
        } catch (IOException e) {
            log.error("@LoginController@ logout fail", e);
        }
    }
}
