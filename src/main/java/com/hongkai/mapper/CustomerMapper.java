package com.hongkai.mapper;

import java.util.List;

import com.hongkai.domain.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Mapper
@Service
public interface CustomerMapper {
    @Insert("INSERT INTO customer(name,pinyin) VALUES(#{name},#{pinyin})")
    void addCustomer(@Param("name") String name, @Param("pinyin") String pinyin);

    @Select("SELECT id,name,cost,store, (cost-store) as ysk,session_id,last_login_time FROM customer WHERE id = #{id}")
    Customer getCustomerById(@Param("id") Integer id);

    @Select("SELECT id,name FROM customer WHERE name = #{name}")
    Customer findByName(@Param("name") String name);

    @Select("SELECT id,name,create_time FROM customer WHERE name like CONCAT('%', #{name}, '%') limit #{limitNum}")
    List<Customer> searchByName(@Param("name") String name, @Param("limitNum") Integer limitNum);

    @Select("SELECT id,name,create_time,last_time FROM customer ORDER BY customer.pinyin")
    List<Customer> getAllCustomers();

    @Update("UPDATE customer set last_time = #{time} WHERE name = #{name}")
    void updateTradeTime(@Param("name") String name, @Param("time") String time);

    @Update("UPDATE customer set last_login_time = #{time} WHERE id = #{id}")
    void updateLastLoginTime(@Param("id") Integer id, @Param("time") String time);

    @Select("SELECT DISTINCT customer.id,customer.name,customer.pinyin,customer.create_time,customer.last_time "
        + "FROM record "
        + "LEFT JOIN customer ON customer.id = record.customer_id "
        + "WHERE record.type = #{type} "
        + "ORDER BY customer.pinyin"
    )
    List<Customer> getCustomersByType(@Param("type") Integer type);

    @Update("UPDATE customer set pinyin = #{pinyin} WHERE id = #{id}")
    void setPinyin(@Param("id") Integer id, @Param("pinyin") String pinYin);
}
