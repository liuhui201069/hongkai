package com.hongkai.controller;

import com.hongkai.code.Result;
import com.hongkai.domain.Customer;
import com.hongkai.domain.Record;
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
 * @date 17/10/29
 */
@Controller
@RequestMapping(value = "/record")
@Slf4j
public class RecordController {
    @Autowired
    RecordMapper recordMapper;

    @Autowired
    CustomerService customerService;

    /**
     * 个人明细查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result edit(@RequestBody Record record) {
        if (record.getId() == null) {
            return new Result(false, "更新记录的ID不能为空.");
        }

        if (record.getCustomer() == null) {
            return new Result(false, "客户名称不能为空.");
        }

        try {
            if (record.getCustomer() != null) {
                Result result = customerService.addCustomer(record.getCustomer());
                if (!result.isSuccess()) {
                    return result;
                } else {
                    Customer customer = (Customer)result.getResult();
                    record.setCustomerId(customer.getId());
                }
            }
            recordMapper.updateRecord(record);
            return new Result(true, record,"更新成功");
        } catch (Exception e) {
            log.error("@RecordController@ update record fail. record {}", record, e);
            return new Result(false, "系统问题：更新失败");
        }
    }

}
