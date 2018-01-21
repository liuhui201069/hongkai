package com.hongkai.domain;

import java.sql.Timestamp;

import lombok.Data;

/**
 * @author huiliu
 * @date 17/10/22
 */
@Data
public class Account {
    Integer id;
    String username;
    String oldPass;
    String password;
    String createTime;
    Timestamp lastTime;
    Integer disable;
}
