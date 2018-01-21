package com.hongkai.service;

import java.util.List;

import com.hongkai.code.Result;
import com.hongkai.domain.Customer;
import com.hongkai.mapper.CustomerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.DateUtil;
import util.PinYinUtils;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Component
@Slf4j
public class CustomerService {
    @Autowired
    CustomerMapper customerMapper;

    /**
     * 根据客户id获取客户信息
     *
     * @param customerId
     * @return
     */
    public Result getCustomerById(Integer customerId) {
        try {
            Customer customer = customerMapper.getCustomerById(customerId);
            return new Result(customer);
        } catch (Exception e) {
            log.error("@CustomerService@ get customer by id :{}", customerId);
            return new Result(false, "系统原因：获取失败");
        }
    }

    /**
     * 模糊搜索客户列表
     *
     * @return
     */
    public Result addCustomer(String name) {
        try {
            Customer customer = customerMapper.findByName(name);
            if (customer != null) {
                customerMapper.updateTradeTime(name, DateUtil.getTodayTime());
            } else {
                customerMapper.addCustomer(name, PinYinUtils.getPinYin(name));
                customer = customerMapper.findByName(name);
            }
            return new Result(true, customer, "添加客户名称" + name + "成功");
        } catch (Exception e) {
            log.error("@CustomerService@ get all customers fail", e);
            return new Result(false, "添加客户名称" + name + "失败");
        }
    }

    /**
     * 模糊搜索客户列表
     *
     * @return
     */
    public Result searchCustomerList(Customer customer, Integer limit) {
        try {
            return new Result(customerMapper.searchByName(customer.getName(), limit));
        } catch (Exception e) {
            log.error("@CustomerService@ get all customers fail", e);
            return new Result(false, "搜索客户列表失败");
        }
    }

    /**
     * 获取所有客户列表
     *
     * @return
     */
    public Result getCustomerList() {
        try {
            return new Result(customerMapper.getAllCustomers());
        } catch (Exception e) {
            log.error("@CustomerService@ get all customers fail", e);
            return new Result(false, "获取客户列表失败");
        }
    }

    /**
     * 获取所有客户列表
     *
     * @return
     */
    public Result getCustomersByType(Integer type) {
        try {
            return new Result(customerMapper.getCustomersByType(type));
        } catch (Exception e) {
            log.error("@CustomerService@ get all customers fail", e);
            return new Result(false, "获取客户列表失败");
        }
    }

    public Customer getCustomerByName(String customerName) {
        return customerMapper.findByName(customerName);
    }

    public Result generatePinyin() {
        try {
            List<Customer> allCustomers = customerMapper.getAllCustomers();
            for (Customer customer : allCustomers) {
                customerMapper.setPinyin(customer.getId(), PinYinUtils.getPinYin(customer.getName()));
            }
            return new Result(true, "done");
        } catch (Exception e) {
            log.error("@CustomerService@ update pinyin fail", e);
            return new Result(false, "update pinyin fail.");
        }
    }
}
