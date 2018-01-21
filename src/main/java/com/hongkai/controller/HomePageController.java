package com.hongkai.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hongkai.code.Result;
import com.hongkai.domain.AccountRight;
import com.hongkai.service.AccountRightService;
import com.hongkai.service.CustomerService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static util.Constants.ACCOUNT_ID;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Controller
public class HomePageController {
    @Autowired
    AccountRightService accountRightService;

    @RequestMapping(value = "/")
    public String login() {
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/test")
    public String home() {
        return "welcome";
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/top")
    public String top() {
        return "top";
    }

    @RequestMapping(value = "/left")

    public String left(ModelMap model) {
        Integer accountId = Integer.valueOf(MDC.get(ACCOUNT_ID));
        Result accountMenusResult = accountRightService.getMenusOfAccountId(accountId);

        List<AccountRight> accountRights = (List<AccountRight>)accountMenusResult.getResult();
        Map<String, Map<String, String>> menus = new LinkedHashMap<>();
        for (AccountRight accountRight : accountRights) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put(accountRight.getName(), accountRight.getUrl());
            menus.merge(accountRight.getPname(),
                map,
                (a, b) -> {
                    Map<String, String> maps = new LinkedHashMap<>();
                    maps.putAll(a);
                    maps.putAll(b);
                    return maps;
                }
            );
        }

        model.addAttribute("account_menus", menus);
        return "left";
    }

    @RequestMapping(value = "/main_frame")
    public String main_frame() {
        return "main_frame";
    }

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "/pinyin")
    @ResponseBody
    public Result pinyin() {
        return customerService.generatePinyin();
    }
}
