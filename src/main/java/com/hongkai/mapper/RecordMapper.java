package com.hongkai.mapper;

import java.util.List;

import com.hongkai.domain.MoneySum;
import com.hongkai.domain.Record;
import org.apache.ibatis.annotations.Delete;
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
public interface RecordMapper {
    @Insert(
        "INSERT INTO record(customer_id,date,variety_id,num,price,car_price,unit,car_fee,total,car_number,car_owner,"
            + "type) "
            + " VALUES(#{record.customerId},#{record.date},#{record.varietyId},#{record.num},"
            + "#{record.price},#{record.carPrice},#{record.unit},#{record.carFee},#{record.total},#{record.carNumber}"
            + ",#{record.carOwner},#{record.type})")
    void insert(@Param("record") Record record);

    @Select(
        "SELECT record.id as id,customer.id as customerId,customer.name as customer,DATE_FORMAT(date,'%Y-%m-%d') as "
            + "date"
            + ",variety.name as variety,variety_id as varietyId,num,"
            + "price,unit,car_fee,car_price,total,car_number,car_owner,date "
            + "FROM record "
            + "LEFT JOIN customer ON record.customer_id = customer.id "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE customer.id = #{customerId} AND record.date >= #{startDate} AND record.date <= #{endDate} "
            + "ORDER BY customer.pinyin"
    )
    List<Record> getRecordByNameAndTime(@Param("customerId") Integer customerId,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);

    @Select(
        "SELECT record.id as id,customer.id as customerId,customer.name as customer,DATE_FORMAT(date,'%Y-%m-%d') as "
            + "date"
            + ",variety.name as variety,variety_id as varietyId,num,"
            + "price,unit,car_fee,car_price,total,car_number,car_owner,date "
            + "FROM record "
            + "LEFT JOIN customer ON record.customer_id = customer.id "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE record.customer_id = #{customerId} AND record.type = #{type} AND record.date >= #{startDate} AND"
            + " record.date <= #{endDate} "
            + "ORDER BY record.date"
    )
    List<Record> getRecordByCustomerAndTypeAndTime(@Param("customerId") Integer customerId,
                                                   @Param("type") Integer type,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);

    @Select(
        "SELECT record.id as id,customer.id as customerId,customer.name as customer,DATE_FORMAT(date,'%Y-%m-%d') as "
            + "date"
            + ",variety.name as variety,variety_id as varietyId,num,"
            + "price,unit,car_fee,car_price,total,car_number,car_owner,date "
            + "FROM record "
            + "LEFT JOIN customer ON record.customer_id = customer.id "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE record.type = #{type} AND record.date >= #{startDate} AND record.date <= #{endDate} "
            + "ORDER BY customer.pinyin"
    )
    List<Record> getRecordByTypeAndTime(@Param("type") Integer type,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);

    @Select(
        "SELECT customer.name as customer,customer_id,variety.name as variety,variety_id as varietyId,ROUND(SUM(num),2) as "
            + "num,SUM(car_fee) as car_fee,SUM(total) as "
            + "total "
            + "FROM record "
            + "LEFT JOIN customer ON record.customer_id = customer.id "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE customer_id = #{customerId} AND date >= #{startDate} AND date <= #{endDate} GROUP BY variety_id "
            + "ORDER BY customer.pinyin"
    )
    List<Record> groupRecordByVariety(@Param("customerId") Integer customerId,
                                      @Param("startDate") String startDate,
                                      @Param("endDate") String endDate);

    @Select(
        "SELECT customer.name as customer,customer_id,variety.name as variety,variety_id as varietyId,ROUND(SUM(num),2) as "
            + "num,SUM(car_fee) as car_fee,SUM(total) as "
            + "total "
            + "FROM record "
            + "LEFT JOIN customer ON record.customer_id = customer.id "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE date >= #{startDate} AND date <= #{endDate} GROUP BY customer_id,variety_id "
            + "ORDER BY customer.pinyin"
    )
    List<Record> groupRecordByVarietyAndCustomer(@Param("startDate") String startDate,
                                                 @Param("endDate") String endDate);

    @Select(
        "SELECT customer.name as customer,customer_id,variety.name as variety,variety_id as varietyId,ROUND(SUM(num),2) as "
            + "num,SUM(car_fee) as car_fee,SUM(total) as "
            + "total "
            + "FROM record "
            + "LEFT JOIN customer ON record.customer_id = customer.id "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE record.customer_id = #{customerId} AND record.type = #{type} AND date >= #{startDate} AND date <= #{endDate} "
            + "GROUP BY variety_id "
            + "ORDER BY customer.pinyin"
    )
    List<Record> groupRecordByVarietyAndTypeForCustomer(@Param("customerId") Integer customerId,
                                                        @Param("type") Integer type,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);

    @Select(
        "SELECT customer.name as customer,customer_id,variety.name as variety,variety_id as varietyId,ROUND(SUM(num),2) as "
            + "num,SUM(car_fee) as car_fee,SUM(total) as "
            + "total "
            + "FROM record "
            + "LEFT JOIN customer ON record.customer_id = customer.id "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE record.type = #{type} AND date >= #{startDate} AND date <= #{endDate} GROUP BY customer_id,"
            + "variety_id "
            + "ORDER BY customer.pinyin"
    )
    List<Record> groupRecordByVarietyAndCustomerAndType(@Param("type") Integer type,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);

    @Select(
        "SELECT variety.name as variety,variety_id as varietyId,ROUND(SUM(num),2) as num,SUM(car_fee) as car_fee,SUM(total) as"
            + " total "
            + "FROM record "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE date >= #{startDate} AND date <= #{endDate} GROUP BY variety_id"
    )
    List<Record> groupAllRecordByVariety(@Param("startDate") String startDate,
                                         @Param("endDate") String endDate);

    @Select(
        "SELECT variety.name as variety,variety_id as varietyId,ROUND(SUM(num),2) as num,SUM(car_fee) as car_fee,SUM(total) as"
            + " total "
            + "FROM record "
            + "LEFT JOIN variety ON variety.id = record.variety_id "
            + "WHERE type = #{type} AND date >= #{startDate} AND date <= #{endDate} GROUP BY variety_id"
    )
    List<Record> groupRecordByVarietyAndType(@Param("type") Integer type,
                                             @Param("startDate") String startDate,
                                             @Param("endDate") String endDate);

    @Select("SELECT customer.id as customerId,customer.name as customer,SUM(record.total) as cost "
        + "FROM record "
        + "LEFT JOIN customer ON record.customer_id = customer.id "
        + "WHERE type = #{type} AND record.date >= #{startDate} AND record.date <= #{endDate} GROUP BY customer_id "
        + "ORDER BY customer.pinyin"
    )
    List<MoneySum> groupRecordByCustomerAndType(@Param("type") Integer type,
                                                @Param("startDate") String startDate,
                                                @Param("endDate") String endDate);

    @Delete("DELETE FROM record WHERE id = #{id}")
    void delete(@Param("id") Integer recordId);

    @Update("UPDATE record "
        + "SET customer_id = #{record.customerId},date = #{record.date}"
        + ",num = #{record.num},variety_id = #{record.varietyId},unit = #{record.unit}"
        + ",price = #{record.price},car_price = #{record.carPrice},car_fee = #{record.carFee}"
        + ",total = #{record.total},car_number = #{record.carNumber},car_owner = #{record.carOwner} "
        + "WHERE id = #{record.id}")
    void updateRecord(@Param("record") Record record);

    @Select("SELECT sum(record.total) as total "
            + "FROM record "
            + "WHERE record.customer_id = #{customerId} AND record.type = #{type} AND record.date >= #{startDate} AND "
            + " record.date <= #{endDate} "
    )
    Integer sumTotalByCustomer(@Param("customerId")Integer customerId,
                           @Param("type")Integer type,
                           @Param("startDate")String startDate,
                           @Param("endDate")String endDate);

    @Select("SELECT sum(record.total) as total "
        + "FROM record "
        + "WHERE record.customer_id = #{customerId} AND record.type = #{type} AND "
        + " record.date < #{endDate} "
    )
    Integer sumLastTotalByCustomer(@Param("customerId")Integer customerId,
                               @Param("type")Integer type,
                               @Param("endDate")String endDate);


    @Select("SELECT customer.id as customerId,customer.name as customer,customer.pinyin as pinyin,sum(record.total) as total "
        + "FROM record "
        + "LEFT JOIN customer ON record.customer_id = customer.id "
        + "WHERE record.type = #{type} AND record.date >= #{startDate} AND "
        + " record.date <= #{endDate} "
        + " group by record.customer_id"
    )
    List<Record> groupTotalByCustomer(@Param("type")Integer type,
                                      @Param("startDate")String startDate,
                                      @Param("endDate")String endDate);

    @Select("SELECT customer.id as customerId,customer.name as customer,customer.pinyin as pinyin,sum(record.total) as total "
        + "FROM record "
        + "LEFT JOIN customer ON record.customer_id = customer.id "
        + "WHERE record.type = #{type} AND "
        + " record.date < #{endDate} "
        + " group by record.customer_id"
    )
    List<Record> groupLastTotalByCustomer(@Param("type")Integer type,
                                          @Param("endDate")String endDate);
}
