package com.hongkai.controller;

import java.util.List;

import com.google.common.base.Splitter;
import com.hongkai.code.Result;
import com.hongkai.domain.Account;
import com.hongkai.mapper.MenuMapper;
import com.hongkai.service.AccountService;
import com.hongkai.service.CustomerService;
import com.hongkai.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.DateUtil;

/**
 * @author huiliu
 * @date 17/10/22
 */
@Controller
@RequestMapping(value = "/account")
@Slf4j
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    CustomerService customerService;

    @Autowired
    MenuService menuService;

    /**
     * 所有账号显示页面
     *
     * @return
     */
    @RequestMapping(value = "/list_page")
    public String listPage(ModelMap model) {
        model.put("result", accountService.getAllAccounts());
        return "/account/list_page";
    }

    /**
     * 添加账号页面
     *
     * @return
     */
    @RequestMapping(value = "/add_page")
    public String addPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/account/add_page";
    }

    /**
     * 添加账号页面
     *
     * @return
     */
    @RequestMapping(value = "/edit_page")
    public String editPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/account/edit_page";
    }

    /**
     * 添加账号接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Account account) {
        return accountService.addAccount(account.getUsername(), account.getPassword());
    }

    /**
     * 添加账号接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Account account) {
        Result result = accountService.auth(account.getUsername(), account.getOldPass());
        return accountService.updatePassword(account.getUsername(), account.getPassword());
    }

    /**
     * 禁用账号接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disable")
    public Result disable(@RequestParam("username") String username) {
        return accountService.disableAccount(username);
    }

    /**
     * 禁用账号接口
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/enable")
    public Result enable(@RequestParam("username") String username) {
        return accountService.enableAccount(username);
    }


    @RequestMapping(value = "/right_page")
    public String lrPage(ModelMap model) {
        model.put("accounts", accountService.getAvailableAccounts());
        model.put("menus", menuService.getAllMenus());
        return "/account/right_page";
    }

    @ResponseBody
    @RequestMapping(value = "/set_right",method = RequestMethod.POST)
    public Result setRight(@RequestParam("accountId")Integer accountId,@RequestParam("ids")String ids) {
        List<String> idList = Splitter.on(",").splitToList(ids);
        return accountService.setRight(accountId,idList);
    }


}
