package com.hongkai.domain;

import lombok.Data;

/**
 * @author huiliu
 * @date 17/10/28
 */
@Data
public class Money {
    Integer id;
    Integer customerId;
    String customer;
    String pinyin;
    String date;
    Integer num;
    Integer type;
    int discount;
}
