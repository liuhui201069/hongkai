package com.hongkai.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hongkai.code.Result;
import com.hongkai.domain.Money;
import com.hongkai.domain.MoneySum;
import com.hongkai.domain.Record;
import com.hongkai.mapper.MoneyMapper;
import com.hongkai.mapper.RecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Component
@Slf4j
public class LuruService {

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    MoneyMapper moneyMapper;

    /**
     * 添加交易记录
     *
     * @return
     */
    public Result addRecord(Record record) {
        try {
            if (StringUtils.isEmpty(record.getCustomerId())) {
                return new Result(false, "客户不能为空");
            }
            if (StringUtils.isEmpty(record.getDate())) {
                return new Result(false, "日期不能为空");
            }
            if (record.getVarietyId() == null) {
                return new Result(false, "品种不能为空");
            }
            if (StringUtils.isEmpty(record.getNum())) {
                return new Result(false, "数量不能为空");
            }
            recordMapper.insert(record);
        } catch (Exception e) {
            log.error("@LuruService@ add variety {} fail.", record, e);
            return new Result(false, "系统原因，添加失败");
        }
        return new Result(true, "录入成功");
    }

    /**
     * 获取某个客户某段时间内的所有记录
     *
     * @param customerId
     * @param startDate
     * @param endDate
     * @return
     */
    public Result allRecords(Integer customerId, String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.getRecordByNameAndTime(customerId, startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@ allRecords fail. customerId:{},startDate:{},endDate:{}", customerId, startDate,
                endDate, e);
            return new Result(false, String.format("查询客户%s从%s到%s的详细列表失败", customerId, startDate, endDate));
        }
    }

    /**
     * 获取某个类型业务某段时间内的所有记录
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    public Result allRecordsByType(Integer type, String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.getRecordByTypeAndTime(type, startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@ allRallRecordsByType fail. type:{},startDate:{},endDate:{}", type, startDate,
                endDate, e);
            return new Result(false, String.format("查询业务从%s到%s的详细列表失败", type, startDate, endDate));
        }
    }

    /**
     * 获取某个类型业务某段时间内的所有记录
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    public Result allRecordsByCustomerAndType(Integer customerId,Integer type, String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.getRecordByCustomerAndTypeAndTime(customerId,type, startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@ allRallRecordsByType fail. type:{},startDate:{},endDate:{}", type, startDate,
                endDate, e);
            return new Result(false, String.format("查询业务从%s到%s的详细列表失败", type, startDate, endDate));
        }
    }

    /**
     * 获取某个客户某段时间内按照品种汇总的所有记录
     *
     * @param customerId
     * @param startDate
     * @param endDate
     * @return
     */
    public Result sumRecordByVariety(Integer customerId, String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.groupRecordByVariety(customerId, startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@sumRecordByVariety fail.name:{},start:{},end:{}", customerId, startDate, endDate,
                e);
            return new Result(false, String.format("查询客户%s从%s到%s的个人汇总失败", customerId, startDate, endDate));
        }
    }

    /**
     * 获取某个客户某段时间内按照品种汇总的所有记录
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Result sumRecordByVariety(String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.groupAllRecordByVariety(startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@sumRecordByVariety fail.start:{},end:{}", startDate, endDate, e);
            return new Result(false, String.format("查询从%s到%s的按品种汇总失败", startDate, endDate));
        }
    }

    /**
     * 获取某段时间内按照客户和品种汇总的所有记录
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Result sumRecordByVarietyAndCustomer(String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.groupRecordByVarietyAndCustomer(startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@sumRecordByVarietyAndCustomer fail.start:{},end:{}", startDate, endDate, e);
            return new Result(false, String.format("查询从%s到%s的按客户和品种汇总失败", startDate, endDate));
        }
    }


    /**
     * 获取某段时间内按照客户和品种汇总的所有记录
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Result sumRecordByVarietyAndTypeForCustomer(Integer customerId,Integer type, String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.groupRecordByVarietyAndTypeForCustomer(customerId,type, startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@sumRecordByVarietyAndCustomer fail.start:{},end:{}", startDate, endDate, e);
            return new Result(false, String.format("查询从%s到%s的按客户和品种汇总失败", startDate, endDate));
        }
    }

    /**
     * 获取某段时间内按照客户和品种汇总的所有记录
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Result sumRecordByVarietyAndCustomerAndType(Integer type, String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.groupRecordByVarietyAndCustomerAndType(type, startDate, endDate);
            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@sumRecordByVarietyAndCustomer fail.start:{},end:{}", startDate, endDate, e);
            return new Result(false, String.format("查询从%s到%s的按客户和品种汇总失败", startDate, endDate));
        }
    }

    /**
     * 获取某段时间内按照客户和品种汇总的所有记录
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public Result sumRecordByVarietyAndType(Integer type, String startDate, String endDate) {
        try {
            List<Record> records = recordMapper.groupRecordByVarietyAndType(type, startDate, endDate);
            if(records.size() > 0){
                Record totalRecord = new Record();
                int total = 0;
                float num = 0;
                int carFee = 0;
                for(Record record : records){
                    total = total + record.getTotal();
                    num = num + Float.valueOf(record.getNum());
                    carFee = carFee +  record.getCarFee();
                }
                totalRecord.setVariety("合计：");
                totalRecord.setTotal(total);
                totalRecord.setNum(num);
                totalRecord.setCarFee(carFee);
                records.add(totalRecord);
            }

            return new Result(records);
        } catch (Exception e) {
            log.error("@LuruService@sumRecordByVarietyAndCustomer fail.start:{},end:{}", startDate, endDate, e);
            return new Result(false, String.format("查询从%s到%s的按客户和品种汇总失败", startDate, endDate));
        }
    }

    /**
     * 现金业务录入
     *
     * @param money
     * @return
     */
    public Result addMoney(Money money) {
        try {
            if (money.getCustomerId() == null) {
                return new Result(false, "客户ID不能为空");
            }
            if (money.getDate() == null) {
                return new Result(false, "日期不能为空");
            }
            if (money.getNum() == null) {
                return new Result(false, "数量不能为空");
            }
            moneyMapper.addMoney(money);
            return new Result(true, "录入成功");
        } catch (Exception e) {
            log.error("@LuruService@ addMoney fail. money {}} ", money, e);
            return new Result(false, "系统原因:录入失败");
        }
    }

    /**
     * 不同业务按照分客户在某段时间段内的汇总
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     */
    public Result sumRecordByCustomerAndType(Integer type, String startDate, String endDate) {
        try {
            List<MoneySum> records = recordMapper.groupRecordByCustomerAndType(type, startDate, endDate);
            List<MoneySum> moneySums = moneyMapper.sumMoneyByCustomerAndType(type, startDate, endDate);

            Set<Integer> ids = Sets.newLinkedHashSet();
            Map<Integer, MoneySum> costSumMap = Maps.newHashMap();
            for(MoneySum record : records){
                ids.add(record.getCustomerId());
                costSumMap.put(record.getCustomerId(), record);
            }

            Map<Integer, MoneySum> storeSumMap = Maps.newHashMap();
            for(MoneySum moneySum:moneySums){
                ids.add(moneySum.getCustomerId());
                storeSumMap.put(moneySum.getCustomerId(), moneySum);
            }


            List<MoneySum> results = Lists.newArrayList();
            for (Integer id : ids) {
                Integer cost = costSumMap.get(id) == null ? 0 : costSumMap.get(id).getCost();
                Integer store = storeSumMap.get(id) == null ? 0 : storeSumMap.get(id).getStore();
                Integer discount = storeSumMap.get(id) == null ?  0 : storeSumMap.get(id).getDiscount();
                Integer ysk = cost - store - discount;

                MoneySum moneySum = new MoneySum();
                if(costSumMap.containsKey(id)){
                    MoneySum record = costSumMap.get(id);
                    moneySum.setCustomer(record.getCustomer());
                    moneySum.setCustomerId(record.getCustomerId());
                }else{
                    MoneySum record = storeSumMap.get(id);
                    moneySum.setCustomer(record.getCustomer());
                    moneySum.setCustomerId(record.getCustomerId());
                }
                moneySum.setCost(cost);
                moneySum.setStore(store);
                moneySum.setDiscount(discount);
                moneySum.setYsk(ysk);

                results.add(moneySum);
            }

            return new Result(results);
        } catch (Exception e) {
            log.error("@LuruService@ sumRecordByCustomerAndType fail. type:{}, startDate:{},endDate:{} ", type,
                startDate, endDate, e);
            return new Result(false, "系统原因:查询失败");
        }

    }
}
