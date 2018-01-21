package com.hongkai.controller;

import com.hongkai.code.Result;
import com.hongkai.domain.Customer;
import com.hongkai.domain.Money;
import com.hongkai.domain.Record;
import com.hongkai.service.CustomerService;
import com.hongkai.service.LuruService;
import com.hongkai.service.VarietyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import util.DateUtil;

import static util.Constants.HTYW;
import static util.Constants.XJYW;
import static util.Constants.ZXYW;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Controller
@RequestMapping(value = "/lr")
@Slf4j
public class LuruController {

    @Autowired
    LuruService luruService;

    @Autowired
    CustomerService customerService;

    @Autowired
    VarietyService varietyService;

    @RequestMapping(value = "/luru_page")
    public String list_page(ModelMap model) {
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/lr/luru_page";
    }

    @RequestMapping(value = "/xjyw_page")
    public String xjyw_page(ModelMap model) {
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/lr/xjyw_page";
    }

    @RequestMapping(value = "/htyw_page")
    public String htyw_page(ModelMap model) {
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/lr/htyw_page";
    }

    @RequestMapping(value = "/zxyw_page")
    public String zxyw_page(ModelMap model) {
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/lr/zxyw_page";
    }

    @RequestMapping(value = "/yskgl_page")
    public String yskgl_page(ModelMap model) {
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/lr/yskgl_page";
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result list(@RequestBody Record record) {
        Result result = customerService.addCustomer(record.getCustomer());
        if (!result.isSuccess()) {
            return result;
        }
        Customer customer = (Customer)result.getResult();
        record.setCustomerId(customer.getId());
        return luruService.addRecord(record);
    }

    @ResponseBody
    @RequestMapping(value = "/xjyw_add", method = RequestMethod.POST)
    public Result xjyw_add(@RequestBody Record record) {
        record.setCustomerId(1);
        record.setType(XJYW);
        return luruService.addRecord(record);
    }

    @ResponseBody
    @RequestMapping(value = "/htyw_add", method = RequestMethod.POST)
    public Result htyw_add(@RequestBody Record record) {
        return addRecord(record, HTYW);
    }

    @ResponseBody
    @RequestMapping(value = "/zxyw_add", method = RequestMethod.POST)
    public Result zxyw_add(@RequestBody Record record) {
        return addRecord(record, ZXYW);
    }

    @ResponseBody
    @RequestMapping(value = "/yskgl_add", method = RequestMethod.POST)
    public Result yskgl_add(@RequestBody Money money) {
        if (money.getCustomerId() == null) {
            Result result = customerService.addCustomer(money.getCustomer());
            if (!result.isSuccess()) {
                return result;
            } else {
                Customer customer = (Customer)result.getResult();
                money.setCustomerId(customer.getId());
            }
        }
        return luruService.addMoney(money);
    }

    @ResponseBody
    @RequestMapping(value = "/search_customer", method = RequestMethod.POST)
    public Result search(Customer customer) {
        Integer defaultLimit = 10;
        return customerService.searchCustomerList(customer, defaultLimit);
    }


    private Result addRecord(Record record, Integer type ){
        if (record.getCustomerId() == null) {
            Result result = customerService.addCustomer(record.getCustomer());
            if (!result.isSuccess()) {
                return result;
            } else {
                Customer customer = (Customer)result.getResult();
                record.setCustomerId(customer.getId());
            }
        }
        record.setType(type);
        return luruService.addRecord(record);
    }
}
