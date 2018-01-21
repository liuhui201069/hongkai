package com.hongkai.domain;

import java.sql.Timestamp;

import lombok.Data;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Data
public class Customer {
    Integer id;
    String name;
    Integer cost;
    Integer store;
    Integer ysk;
    Timestamp createTime;
    Timestamp lastTime;
}
