package com.hongkai.service;

import java.util.List;

import com.hongkai.code.Result;
import com.hongkai.domain.Account;
import com.hongkai.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.DateUtil;
import util.SHAencrypt;

/**
 * @author huiliu
 * @date 17/10/22
 */
@Component
@Slf4j
public class AccountService {
    @Autowired
    AccountMapper accountMapper;

    /**
     * 添加账号
     *
     * @param username
     * @param password
     */
    public Result addAccount(String username, String password) {
        try {
            if (StringUtils.isEmpty(username)) {
                return new Result(false, "用户名不能为空");
            }

            if (StringUtils.isEmpty(password)) {
                return new Result(false, "密码不能为空");
            }

            Account account = accountMapper.findAccountByUsername(username);
            if (account == null) {
                accountMapper.addAccount(username, SHAencrypt.encryptSHA(password));
                return new Result(true, "添加账号" + username + "成功");
            } else {
                return new Result(false, "账号名：" + username + "已存在");
            }

        } catch (Exception e) {
            log.error("@AccountService@ addAccount fail. username:{},password:{}", e);
            return new Result(false, "添加账号失败");
        }
    }

    /**
     * 获取所有账号列表
     *
     * @return
     */
    public Result getAvailableAccounts() {
        try {
            List<Account> accounts = accountMapper.getAvailableAccounts();
            return new Result(accounts);
        } catch (Exception e) {
            log.error("@AccountService@ getAllAccounts fail.", e);
            return new Result(false, "获取账号列表失败");
        }
    }

    /**
     * 获取所有账号列表
     *
     * @return
     */
    public Result getAllAccounts() {
        try {
            List<Account> accounts = accountMapper.getAllAccounts();
            return new Result(accounts);
        } catch (Exception e) {
            log.error("@AccountService@ getAllAccounts fail.", e);
            return new Result(false, "获取账号列表失败");
        }
    }

    /**
     * 更新账号最后登录时间
     *
     * @param id
     */
    public Result updateLastTime(Integer id) {
        try {
            String time = DateUtil.getTodayTime();
            accountMapper.updateLastTime(id, time);
            return new Result(true, "更新账号" + id + "最后登录时间成功");
        } catch (Exception e) {
            log.error("@AccountService@ updateLastTime fail. id:{}", id, e);
            return new Result(false, "更新账号" + id + "最后登录时间失败");
        }
    }

    /**
     * 禁用账号
     *
     * @param username 要被禁用的账号username
     */
    public Result disableAccount(String username) {
        try {
            Account account = accountMapper.findAccountByUsername(username);
            if (account != null) {
                accountMapper.disableAccount(account.getId());
            } else {
                return new Result(false, "账号" + account.getUsername() + "不存在");
            }

            return new Result(true, "禁用账号" + account.getUsername() + "成功");
        } catch (Exception e) {
            log.error("@AccountService@ disableAccount fail. username:{}", username, e);
            return new Result(false, "禁用账号" + username + "失败");
        }
    }

    /**
     * 启用账号
     *
     * @param username 要被禁用的账号username
     */
    public Result enableAccount(String username) {
        try {
            Account account = accountMapper.findAccountByUsername(username);
            if (account != null) {
                accountMapper.enableAccount(account.getId());
            } else {
                return new Result(false, "账号" + account.getUsername() + "不存在");
            }

            return new Result(true, "启用账号" + account.getUsername() + "成功");
        } catch (Exception e) {
            log.error("@AccountService@ enableAccount fail. username:{}", username, e);
            return new Result(false, "启用账号" + username + "失败");
        }
    }

    /**
     * 重置账号密码
     *
     * @param username
     * @param oldPassword
     * @param newPassword
     */
    public Result resetPassword(String username, String oldPassword, String newPassword) {
        try {
            Account account = accountMapper.findAccountByUsername(username);
            if (account != null) {
                if (StringUtils.equals(account.getPassword(), SHAencrypt.encryptSHA(oldPassword))) {
                    accountMapper.resetPassword(account.getId(), SHAencrypt.encryptSHA(newPassword));
                    return new Result(true, "重置密码成功");
                } else {
                    return new Result(false, "密码不正确");
                }
            } else {
                return new Result(false, "账号" + account.getUsername() + "不存在");
            }
        } catch (Exception e) {
            log.error("@AccountService@ resetPassword fail. username:{}", username, e);
            return new Result(false, "系统问题，重置密码失败");
        }
    }

    /**
     * 验证用户名，密码
     *
     * @param username
     * @param password
     */
    public Result auth(String username, String password) {
        try {
            if (StringUtils.isEmpty(username)) {
                return new Result(false, "用户名不能为空");
            }

            if (StringUtils.isEmpty(password)) {
                return new Result(false, "密码不能为空");
            }

            Account account = accountMapper.findAccountByUsername(username);
            if (account == null) {
                return new Result(false, "用户名不存在");
            } else {
                if (StringUtils.equals(account.getPassword(), SHAencrypt.encryptSHA(password))) {
                    if (account.getDisable() == 0) {
                        return new Result(true, account, "登录成功");
                    } else {
                        return new Result(true, "账号已禁用");
                    }
                } else {
                    return new Result(false, "用户名、密码错误");
                }
            }
        } catch (Exception e) {
            log.error("@AccountService@ auth fail. username :{} , password:{}", username, password);
            return new Result(false, "系统错误，登录失败");
        }
    }

    /**
     * 根据sessionId的获取用户id
     *
     * @param sessionId
     * @return
     */
    public Result getAccountBySessionId(String sessionId) {
        try {
            return new Result(accountMapper.getAccountBySessionId(sessionId));
        } catch (Exception e) {
            log.error("@CustomerService@ getCustomerBySessionId fail. sessionId:{}", sessionId, e);
            return new Result(false, "获取用户信息失败");
        }

    }

    /**
     * 更新session id
     *
     * @param id
     */
    public Result updateSessionId(Integer id, String sessionId) {
        try {
            accountMapper.updateSessionIdAndLastTime(id, sessionId, DateUtil.getTodayTime());
            return new Result(true);
        } catch (Exception e) {
            log.error("@CustomerService@ getCustomerBySessionId fail. sessionId:{}", sessionId, e);
            return new Result(false, "更新用户登录信息失败");
        }

    }

    public Result setRight(Integer accountId, List<String> idList) {
        try {
            accountMapper.deleteRight(accountId);
            accountMapper.insertRight(accountId, idList);
            return new Result(true, "设置权限成功");
        } catch (Exception e) {
            log.error("@AccountService@ set right fail.", e);
            return new Result(false, "系统问题：设置权限失败");
        }
    }

    public Result updatePassword(String username, String password) {
        try {
            accountMapper.updatePassword(username,SHAencrypt.encryptSHA(password));
            return new Result(true, "修改密码成功");
        } catch (Exception e) {
            log.error("@AccountService@ updatePassword {} fail.", e,username);
            return new Result(false, "系统问题：修改密码失败");
        }
    }
}
