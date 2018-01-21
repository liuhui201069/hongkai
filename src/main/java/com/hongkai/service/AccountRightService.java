package com.hongkai.service;

import com.hongkai.code.Result;
import com.hongkai.mapper.AccountRightMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huiliu
 * @date 17/10/30
 */
@Component
@Slf4j
public class AccountRightService {
    @Autowired
    AccountRightMapper accountRightMapper;

    public Result getMenusOfAccountId(Integer accountId) {
        try {
            return new Result(accountRightMapper.getMenusOfAccountId(accountId));
        } catch (Exception e) {
            log.error("@MenuSerice@ getMenusOfAccountId fail. accountId {}", accountId, e);
            return new Result(false, "获取权限列表失败");
        }
    }
}
