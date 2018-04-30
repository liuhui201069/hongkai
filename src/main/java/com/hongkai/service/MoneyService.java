package com.hongkai.service;

import java.util.List;

import com.hongkai.code.Result;
import com.hongkai.domain.Money;
import com.hongkai.domain.Record;
import com.hongkai.mapper.MenuMapper;
import com.hongkai.mapper.MoneyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huiliu
 * @date 17/12/9
 */
@Component
@Slf4j
public class MoneyService {
    @Autowired
    MoneyMapper moneyMapper;

    public Result allMoneysByType(Integer type, String startDate, String endDate) {
        try {
            List<Money> moneys = moneyMapper.getMoneyByTypeAndTime(type, startDate, endDate);
            return new Result(moneys);
        } catch (Exception e) {
            log.error("@MoneyService@ allMoneysByType fail. type:{},startDate:{},endDate:{}", type, startDate,
                endDate, e);
            return new Result(false, String.format("查询应收款明细从%s到%s的详细列表失败", type, startDate, endDate));
        }
    }

    public Result allMoneysByTypeAndCustomer(Integer customerId, Integer type, String startDate, String endDate) {
        try {
            List<Money> moneys = moneyMapper.getMoneyByCustomerAndTypeAndTime(customerId,type, startDate, endDate);
            return new Result(moneys);
        } catch (Exception e) {
            log.error("@MoneyService@ allMoneysByType fail. type:{},startDate:{},endDate:{}", type, startDate,
                endDate, e);
            return new Result(false, String.format("查询应收款明细从%s到%s的详细列表失败", type, startDate, endDate));
        }
    }

    /**
     * 本期付款
     * @return
     */
    public Integer sumTotalByCustomer(Integer customerId, Integer htyw, String startDate, String endDate) {
        return moneyMapper.sumTotalByCustomer(customerId,htyw,startDate,endDate);
    }

    public Integer sumLastTotalByCustomer(Integer customerId, Integer htyw, String endDate) {
        return moneyMapper.sumLastTotalByCustomer(customerId,htyw,endDate);
    }
}
