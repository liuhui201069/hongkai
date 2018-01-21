package com.hongkai.mapper;

import java.util.List;

import com.hongkai.domain.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

/**
 * @author huiliu
 * @date 17/10/22
 */
@Mapper
@Service
public interface AccountMapper {
    @Insert("INSERT INTO account(username,password) VALUES(#{username},#{password})")
    void addAccount(@Param("username") String username, @Param("password") String password);

    @Select("SELECT id,username,create_time,disable,last_time FROM account")
    List<Account> getAllAccounts();

    @Update("UPDATE account set last_time = #{time} WHERE id = #{id}")
    void updateLastTime(@Param("id") Integer id, @Param("time") String time);

    @Select("SELECT id,username,password,create_time,last_time,disable FROM account WHERE username = #{username}")
    Account findAccountByUsername(@Param("username") String username);

    @Select("SELECT id,username,create_time,last_time,session FROM account WHERE session = #{session}")
    Account findAccountBySession(@Param("session") Integer session);

    @Update("UPDATE account set disable = 1 WHERE id = #{id}")
    void disableAccount(@Param("id") Integer id);

    @Update("UPDATE account set password = #{password} WHERE id = #{id}")
    void resetPassword(@Param("id") Integer id, @Param("password") String password);

    @Update("UPDATE account set disable = 0 WHERE id = #{id}")
    void enableAccount(Integer id);

    @Select("SELECT id,username,session_id,disable,last_time "
        + "FROM account WHERE session_id = #{sessionId}")
    Account getAccountBySessionId(@Param("sessionId") String sessionId);

    @Update("UPDATE account set session_id = #{sessionId},last_time = #{todayTime}  WHERE id = #{id}")
    void updateSessionIdAndLastTime(@Param("id") Integer id,
                                    @Param("sessionId") String sessionId,
                                    @Param("todayTime") String todayTime
    );

    @Select("SELECT id,username,create_time,disable,last_time "
        + "FROM account "
        + "WHERE disable = 0")
    List<Account> getAvailableAccounts();

    @Insert({"<script>",
        "INSERT INTO account_right(account_id,menu_id) VALUES",
        "<foreach item='item' index='index' collection='list' separator=',' close=';'>",
        "(#{accountId},#{item})",
        "</foreach>",
        "</script>"})
    void insertRight(@Param("accountId") Integer accountId, @Param("list") List<String> idList);

    @Delete("DELETE FROM account_right WHERE account_id = #{accountId}")
    void deleteRight(@Param("accountId") Integer accountId);

    @Update("UPDATE account "
        + "SET password = #{password} "
        + "WHERE username = #{username}")
    void updatePassword(@Param("username") String username,
                        @Param("password") String password);
}
