package com.hongkai.mapper;

import java.util.List;

import com.hongkai.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * @author huiliu
 * @date 17/10/29
 */
@Mapper
@Service
public interface MenuMapper {
    @Select("SELECT id,pname,name FROM menu")
    List<Menu> getAllMenus();
}
