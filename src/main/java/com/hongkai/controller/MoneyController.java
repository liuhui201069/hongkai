package com.hongkai.controller;

import com.hongkai.code.Result;
import com.hongkai.domain.Customer;
import com.hongkai.domain.Money;
import com.hongkai.domain.Record;
import com.hongkai.mapper.MoneyMapper;
import com.hongkai.mapper.RecordMapper;
import com.hongkai.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huiliu
 * @date 17/12/9
 */
@Controller
@RequestMapping(value = "/money")
@Slf4j
public class MoneyController {
    @Autowired
    MoneyMapper moneyMapper;

    @Autowired
    CustomerService customerService;

    /**
     * 个人明细查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody Money money) {
        if (money.getId() == null) {
            return new Result(false, "更新记录的ID不能为空.");
        }

        if (money.getCustomer() == null) {
            return new Result(false, "客户名称不能为空.");
        }

        try {
            if (money.getCustomer() != null) {
                Result result = customerService.addCustomer(money.getCustomer());
                if (!result.isSuccess()) {
                    return result;
                } else {
                    Customer customer = (Customer)result.getResult();
                    money.setCustomerId(customer.getId());
                }
            }
            moneyMapper.updateRecord(money);
            return new Result(true, money,"更新成功");
        } catch (Exception e) {
            log.error("@RecordController@ update record fail. record {}", money, e);
            return new Result(false, "系统问题：更新失败");
        }
    }

}
