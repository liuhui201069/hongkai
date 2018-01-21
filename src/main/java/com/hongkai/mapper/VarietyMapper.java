package com.hongkai.mapper;

import java.util.List;

import com.hongkai.domain.Variety;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Mapper
@Service
public interface VarietyMapper {
    @Insert("INSERT INTO variety(name) VALUES(#{name})")
    void insert(Variety name);

    @Select("SELECT name FROM variety WHERE name = #{name}")
    Variety findByName(@Param("name") String name);

    @Select("SELECT id,name,create_time FROM variety order by create_time")
    List<Variety> getAllVariety();

    @Select("UPDATE variety set name = #{newVariety} WHERE name = #{variety}")
    void updateVariety(@Param("variety") String variety, @Param("newVariety") String newVariety);
}
