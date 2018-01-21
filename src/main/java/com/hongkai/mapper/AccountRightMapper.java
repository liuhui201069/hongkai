package com.hongkai.mapper;

import java.util.List;

import com.hongkai.domain.AccountRight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * @author huiliu
 * @date 17/10/29
 */
@Mapper
@Service
public interface AccountRightMapper {
    @Select("SELECT account_right.id,account_right.account_id,account_right.menu_id,menu.name as name,menu.pname as pname,menu.url as url "
        + "FROM account_right "
        + "LEFT JOIN menu ON account_right.menu_id = menu.id "
        + "WHERE account_id = #{accountId}")
    List<AccountRight> getMenusOfAccountId(Integer accountId);
}
