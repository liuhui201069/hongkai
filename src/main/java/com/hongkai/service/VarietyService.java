package com.hongkai.service;

import com.hongkai.code.Result;
import com.hongkai.domain.Variety;
import com.hongkai.mapper.VarietyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Component
@Slf4j
public class VarietyService {
    @Autowired
    VarietyMapper varietyMapper;

    /**
     * 添加品种
     *
     * @param variety
     * @return
     */
    public Result addVariety(Variety variety) {
        try {
            Variety findVariety = varietyMapper.findByName(variety.getName());
            if (findVariety == null) {
                varietyMapper.insert(variety);
            } else {
                return new Result(false, "添加失败：品种" + variety.getName() + "已经存在");
            }
        } catch (Exception e) {
            log.error("@VarietyService@ add variety {} fail.", variety, e);
            return new Result(false, "系统原因，添加失败");
        }
        return new Result(true, "添加品种：" + variety.getName() + "成功");
    }

    /**
     * 获取所有品种
     *
     * @return
     */
    public Result getVarietyList() {
        try {
            return new Result(varietyMapper.getAllVariety());
        } catch (Exception e) {
            log.error("@VarietyService@ get all variety fail", e);
            return new Result(false, "获取品种列表失败");
        }
    }

    /**
     * 获取所有品种
     *
     * @return
     */
    public Result updateVarietyName(String variety, String newVariety) {
        try {
            Variety v = varietyMapper.findByName(newVariety);
            if(v== null){
                varietyMapper.updateVariety(variety, newVariety);
            }else{
                return new Result(false, "新品种名称已存在");
            }
            return new Result(true, "修改品种名称成功");
        } catch (Exception e) {
            log.error("@VarietyService@ update variety name fail. variety:{} , newVariety:{}", variety, newVariety, e);
            return new Result(false, "系统问题，修改品种名称失败");
        }
    }
}
