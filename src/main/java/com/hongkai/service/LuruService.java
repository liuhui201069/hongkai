package com.hongkai.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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

import static util.Constants.HTYW;

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

            String firstDay = org.apache.commons.lang3.StringUtils.substringBeforeLast(startDate,"-") + "-" + "01";
            Integer lastCost = recordMapper.sumLastTotalByCustomer(customerId, type, firstDay);
            Integer lastPay = moneyMapper.sumLastTotalByCustomer(customerId, type, firstDay);
            Integer currentCost = recordMapper.sumTotalByCustomer(customerId, type, startDate, endDate);
            Integer currentPay = moneyMapper.sumTotalByCustomer(customerId, type, startDate, endDate);

            lastCost = (lastCost == null )? 0 : lastCost;
            lastPay = (lastPay == null )? 0 : lastPay;
            currentCost = (currentCost == null )? 0 : currentCost;
            currentPay = (currentPay == null )? 0 : currentPay;

            JSONArray arr = new JSONArray();
            JSONObject rest = new JSONObject();
            rest.put("last_rest", lastCost - lastPay);
            rest.put("current_cost", currentCost);
            rest.put("current_pay", currentPay);
            rest.put("current_rest", lastCost + currentCost - lastPay - currentPay);
            arr.add(rest);

            JSONObject rst = new JSONObject();
            rst.put("rest", arr);
            rst.put("records", records);
            return new Result(rst);
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

            String firstDay = org.apache.commons.lang3.StringUtils.substringBeforeLast(startDate,"-") + "-" + "01";
            List<Record> lastCost = recordMapper.groupLastTotalByCustomer(type, firstDay);
            List<Money> lastPay = moneyMapper.groupLastTotalByCustomer(type, firstDay);
            List<Record> currentCost = recordMapper.groupTotalByCustomer(type, startDate, endDate);
            List<Money> currentPay = moneyMapper.groupTotalByCustomer(type, startDate, endDate);

            Map<Integer,String> ids = Maps.newHashMap();
            Map<Integer,String> pinyins = Maps.newHashMap();
            Map<Integer, Integer> lastCostMap = Maps.newHashMap();
            for(Record record : lastCost){
                ids.put(record.getCustomerId(), record.getCustomer());
                pinyins.put(record.getCustomerId(), record.getPinyin());
                lastCostMap.put(record.getCustomerId(), record.getTotal());
            }

            Map<Integer, Integer> lastPayMap = Maps.newHashMap();
            for(Money money: lastPay){
                ids.put(money.getCustomerId(), money.getCustomer());
                pinyins.put(money.getCustomerId(), money.getPinyin());
                lastPayMap.put(money.getCustomerId(), money.getNum());
            }

            Map<Integer, Integer> currentCostMap = Maps.newHashMap();
            for(Record record: currentCost){
                ids.put(record.getCustomerId(), record.getCustomer());
                pinyins.put(record.getCustomerId(), record.getPinyin());
                currentCostMap.put(record.getCustomerId(), record.getTotal());
            }

            Map<Integer, Integer> currentPayMap = Maps.newHashMap();
            for(Money money: currentPay){
                ids.put(money.getCustomerId(), money.getCustomer());
                pinyins.put(money.getCustomerId(), money.getPinyin());
                currentPayMap.put(money.getCustomerId(), money.getNum());
            }

            JSONArray jsonArray = new JSONArray();
            for (Integer id : ids.keySet()) {
                Integer lastCostValue = lastCostMap.get(id) == null ? 0 : lastCostMap.get(id);
                Integer lastPayValue = lastPayMap.get(id) == null ? 0 : lastPayMap.get(id);
                Integer currentCostValue = currentCostMap.get(id) == null ?  0 : currentCostMap.get(id);
                Integer currentPayValue = currentPayMap.get(id) == null ?  0 : currentPayMap.get(id);

                JSONObject rest = new JSONObject();
                rest.put("customer", ids.get(id));
                rest.put("pinyin", pinyins.get(id));
                rest.put("last_rest", lastCostValue - lastPayValue);
                rest.put("current_cost", currentCostValue);
                rest.put("current_pay", currentPayValue);
                rest.put("current_rest", lastCostValue + currentCostValue - lastPayValue - currentPayValue);

                jsonArray.add(rest);
            }

            List l = jsonArray.stream().sorted(Comparator.comparing(
                obj -> {
                    JSONObject jo = (JSONObject) obj;
                    return jo.getString("pinyin");
                }
            )).collect(Collectors.toList());

            return new Result(l);
        } catch (Exception e) {
            log.error("@LuruService@ sumRecordByCustomerAndType fail. type:{}, startDate:{},endDate:{} ", type,
                startDate, endDate, e);
            return new Result(false, "系统原因:查询失败");
        }

    }
}
