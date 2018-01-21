package com.hongkai.domain;

import lombok.Data;

/**
 * @author huiliu
 * @date 17/10/22
 */
@Data
public class AccountRight {
    Integer id;
    Integer accountId;
    Integer menuId;
    String pname;
    String name;
    String url;
}
