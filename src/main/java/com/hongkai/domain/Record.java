package com.hongkai.domain;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.alibaba.fastjson.JSON;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Data
public class Record {
    Integer id;
    Integer customerId;
    String customer;
    String pinyin;
    String date;
    String variety;
    Integer varietyId;
    float num;
    float price;
    float carPrice;
    String unit;
    int carFee;
    Integer total;
    String carNumber;
    String carOwner;
    Integer type;
    String qhfs;

    public String getNum() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        /*
         * setMinimumFractionDigits设置成2
         *
         * 如果不这么做，那么当value的值是100.00的时候返回100
         *
         * 而不是100.00
         */
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(false);

        ////float存储到excel会丢失精度
        //DecimalFormat df = new DecimalFormat("#0.00");
        //String str = df.format(this.num).toString();
        //String[] parts = StringUtils.split(str, "\\.");
        //if (parts.length > 1) {
        //    if("00".equals(parts[1])){
        //        str = parts[0];
        //    }else if("0".equals(parts[1].substring(1))){
        //        str = parts[1].substring(0);
        //    }
        //}
        return nf.format(this.num);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
