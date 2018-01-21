package com.hongkai.mapper;

import java.util.List;

import com.hongkai.domain.Customer;
import com.hongkai.domain.Money;
import com.hongkai.domain.MoneySum;
import com.hongkai.domain.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

/**
 * @author huiliu
 * @date 17/10/28
 */
@Mapper
@Service
public interface MoneyMapper {
    @Insert("INSERT INTO money(customer_id,date,num,type,discount) "
        + "VALUES(#{money.customerId},#{money.date},#{money.num},#{money.type},#{money.discount})")
    void addMoney(@Param("money") Money money);

    @Select(
        "SELECT customer.name as customer,customer_id as customerId,SUM(money.num) as store, SUM(money.discount) as "
            + "discount "
            + "FROM money "
            + "LEFT JOIN customer ON (customer.id = money.customer_id)"
            + "WHERE type = #{type} AND date >= #{startDate} AND date <= #{endDate} GROUP BY customer_id "
            + "ORDER BY customer.pinyin"
    )
    List<MoneySum> sumMoneyByCustomerAndType(@Param("type") Integer type,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    @Select("SELECT DISTINCT customer.id,customer.name,customer.create_time,customer.last_time "
        + "FROM money "
        + "LEFT JOIN customer ON customer.id = money.customer_id "
        + "WHERE record.type = #{type} "
        + "ORDER BY customer.pinyin"
    )
    List<Customer> getCustomersByType(@Param("type") Integer type);

    @Select(
        "SELECT customer.name as customer,customer_id as customerId,money.num, money.discount, date, type "
            + "FROM money "
            + "LEFT JOIN customer ON (customer.id = money.customer_id) "
            + "WHERE type = #{type} AND date >= #{startDate} AND date <= #{endDate} AND customer_id = #{customerId} "
            + "ORDER BY customer.pinyin"
    )
    List<Money> getMoneyByCustomerIdAndTypeAndTime(@Param("customerId") Integer customerId,
                                                   @Param("type") Integer type,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    @Select(
        "SELECT money.id as id,customer.name as customer,customer_id as customerId,money.num, money.discount,DATE_FORMAT(date,'%Y-%m-%d') as date, type "
            + "FROM money "
            + "LEFT JOIN customer ON (customer.id = money.customer_id) "
            + "WHERE type = #{type} AND date >= #{startDate} AND date <= #{endDate} "
            + "ORDER BY customer.pinyin"
    )
    List<Money> getMoneyByTypeAndTime(@Param("type") Integer type,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate);

    @Update("UPDATE money "
        + "SET customer_id = #{money.customerId},date = #{money.date}"
        + ",num = #{money.num},type=#{money.type},discount = #{money.discount} "
        + "WHERE id = #{money.id}")
    void updateRecord(@Param("money") Money money);

    @Select(
        "SELECT money.id as id,customer.name as customer,customer_id as customerId,money.num, money.discount,DATE_FORMAT(date,'%Y-%m-%d') as date, type "
            + "FROM money "
            + "LEFT JOIN customer ON (customer.id = money.customer_id) "
            + "WHERE customer.id = #{customerId} AND type = #{type} AND date >= #{startDate} AND date <= #{endDate} "
            + "ORDER BY customer.pinyin"
    )
    List<Money> getMoneyByCustomerAndTypeAndTime(@Param("customerId") Integer customerId,
                                                 @Param("type") Integer type,
                                                 @Param("startDate") String startDate,
                                                 @Param("endDate") String endDate);
}
