package com.hongkai.service;

import com.hongkai.code.Result;
import com.hongkai.mapper.MenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huiliu
 * @date 17/10/29
 */
@Component
@Slf4j
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

    public Result getAllMenus(){
        try{
            return new Result(menuMapper.getAllMenus());
        }catch (Exception e){
            log.error("@MenuSerice@ get menus fail.",e);
            return new Result(false,"获取菜单列表失败");
        }
    }
}
