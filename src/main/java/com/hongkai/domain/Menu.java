package com.hongkai.domain;

import lombok.Data;
import org.apache.ibatis.annotations.Delete;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Data
public class Menu {
    Integer id;
    String pname;
    String name;
    String url;
}
