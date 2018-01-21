package com.hongkai.domain;

import java.sql.Timestamp;

import lombok.Data;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Data
public class Variety {
    Integer id;
    String name;
    Timestamp createTime;
}
