package com.hongkai.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.hongkai.code.Result;
import com.hongkai.domain.MoneySum;
import com.hongkai.domain.Record;
import com.hongkai.service.LuruService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.DateUtil;

import static util.Constants.HTYW;
import static util.Constants.XJYW;
import static util.Constants.ZXYW;
import static util.Constants.getRequestParams;

/**
 * @author huiliu
 * @date 17/10/29
 */
@Controller
@RequestMapping(value = "/cx")
@Slf4j
public class QueryController {
    @Autowired
    LuruService luruService;

    @RequestMapping(value = "/xjyw_page")
    public String xjywPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/cx/xjyw_page";
    }

    @RequestMapping(value = "/htyw_page")
    public String htywPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/cx/htyw_page";
    }

    @RequestMapping(value = "/zxyw_page")
    public String zxywPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/cx/zxyw_page";
    }

    /**
     * 按现金业务的查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/xjyw_pz_hz", method = RequestMethod.POST)
    public Result xjywSum(@RequestParam("start_date") String startDate,
                          @RequestParam("end_date") String endDate) {
        return luruService.sumRecordByVarietyAndType(XJYW, startDate, endDate);
    }

    /**
     * 按合同业务的查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/htyw_kh_hz", method = RequestMethod.POST)
    public Result htywSum(@RequestParam("start_date") String startDate,
                          @RequestParam("end_date") String endDate) {
        return luruService.sumRecordByCustomerAndType(HTYW, startDate, endDate);
    }

    /**
     * 按装卸业务的查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/zxyw_kh_hz", method = RequestMethod.POST)
    public Result zxywSum(@RequestParam("start_date") String startDate,
                          @RequestParam("end_date") String endDate) {
        return luruService.sumRecordByCustomerAndType(ZXYW, startDate, endDate);
    }

    @ResponseBody
    @RequestMapping(value = "/xjyw_pz_hz_excel", method = RequestMethod.POST)
    public Result xjywPzHzExcel(HttpServletRequest request, HttpServletResponse response) {
        String filename = "现金业务汇总";
        return getXjywExcel(request, response, filename);
    }

    @ResponseBody
    @RequestMapping(value = "/htyw_kh_hz_excel", method = RequestMethod.POST)
    public Result htywKhHzExcel(HttpServletRequest request, HttpServletResponse response) {
        String filename = "合同业务汇总";
        return getExcel(request, response, filename, HTYW);
    }

    @ResponseBody
    @RequestMapping(value = "/zxyw_kh_hz_excel", method = RequestMethod.POST)
    public Result zxywKhHzExcel(HttpServletRequest request, HttpServletResponse response) {
        String filename = "装卸业务汇总";
        return getExcel(request, response, filename, ZXYW);
    }

    public static List<String> YSK_TABLE_HEADER = Lists.newArrayList("客户名称", "上期欠款","本期发生","本期付款","本期下欠");
    public static List<String> XJYW_TABLE_HEADER = Lists.newArrayList("品种", "数量", "运费", "总额");

    private Result getXjywExcel(HttpServletRequest request, HttpServletResponse response, String filename) {
        Map map = getRequestParams(request);
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result = luruService.sumRecordByVarietyAndType(XJYW, startDate, endDate);

        if (result.isSuccess()) {
            List<Record> records = (List<Record>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = filename + "_" + startDate + "_" + endDate;
                response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso8859-1") + ".xls");
                response.setContentType("application/x-download; charset=UTF-8");

                //生成Excel sheet
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet(fileName);

                //设置标题样式
                HSSFCellStyle headStyle = wb.createCellStyle();
                headStyle.setAlignment(HorizontalAlignment.CENTER);
                headStyle.setFillForegroundColor((short)13);
                HSSFFont font = wb.createFont();
                headStyle.setFont(font);

                //设置内容样式
                HSSFCellStyle style = wb.createCellStyle();
                // 创建一个居中格式
                style.setAlignment(HorizontalAlignment.CENTER);

                HSSFRow row = sheet.createRow(0);
                HSSFCell cell;

                //为Excel添加表头
                for (int i = 0; i < XJYW_TABLE_HEADER.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(XJYW_TABLE_HEADER.get(i));
                    cell.setCellStyle(headStyle);
                    //设置列表宽度
                    sheet.setColumnWidth(i, XJYW_TABLE_HEADER.get(i).toString().length() * 800);
                }
                //为Excel添加数据
                for (int j = 0; j < records.size(); j++) {
                    Record record = records.get(j);
                    row = sheet.createRow(j + 1);
                    //第1列
                    cell = row.createCell(0);
                    cell.setCellValue(record.getVariety());
                    cell.setCellStyle(style);
                    //第2列
                    cell = row.createCell(1);
                    cell.setCellValue(record.getNum());
                    cell.setCellStyle(style);
                    //第3列
                    cell = row.createCell(2);
                    cell.setCellValue(record.getCarFee());
                    cell.setCellStyle(style);
                    //第4列
                    cell = row.createCell(3);
                    cell.setCellValue(record.getTotal());
                    cell.setCellStyle(style);
                }

                wb.write(out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(false, "系统问题：导出错误");
            }
        }
        return new Result(true, "导出成功");
    }

    private Result getExcel(HttpServletRequest request, HttpServletResponse response, String filename, Integer type) {
        Map map = getRequestParams(request);
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result;
        List<String> HEADER;
        if (type == XJYW) {
            result = luruService.sumRecordByVarietyAndType(XJYW, startDate, endDate);
            HEADER = XJYW_TABLE_HEADER;
        } else {
            result = luruService.sumRecordByCustomerAndType(type, startDate, endDate);
            HEADER = YSK_TABLE_HEADER;
        }

        if (result.isSuccess()) {
            List records = (List)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = filename + "_" + startDate + "_" + endDate;
                response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso8859-1") + ".xls");
                response.setContentType("application/x-download; charset=UTF-8");

                //生成Excel sheet
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet(fileName);

                //设置标题样式
                HSSFCellStyle headStyle = wb.createCellStyle();
                headStyle.setAlignment(HorizontalAlignment.CENTER);
                headStyle.setFillForegroundColor((short)13);
                HSSFFont font = wb.createFont();
                headStyle.setFont(font);

                //设置内容样式
                HSSFCellStyle style = wb.createCellStyle();
                // 创建一个居中格式
                style.setAlignment(HorizontalAlignment.CENTER);

                HSSFRow row = sheet.createRow(0);
                HSSFCell cell;

                //为Excel添加表头
                for (int i = 0; i < YSK_TABLE_HEADER.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(YSK_TABLE_HEADER.get(i));
                    cell.setCellStyle(headStyle);
                    //设置列表宽度
                    sheet.setColumnWidth(i, YSK_TABLE_HEADER.get(i).toString().length() * 800);
                }
                //为Excel添加数据
                for (int j = 0; j < records.size(); j++) {
                    JSONObject jo = (JSONObject)records.get(j);
                    row = sheet.createRow(j + 1);
                    //第1列
                    cell = row.createCell(0);
                    cell.setCellValue(jo.getString("customer"));
                    cell.setCellStyle(style);
                    //第2列
                    cell = row.createCell(1);
                    cell.setCellValue(jo.getString("last_rest"));
                    cell.setCellStyle(style);
                    //第3列
                    cell = row.createCell(2);
                    cell.setCellValue(jo.getString("current_cost"));
                    cell.setCellStyle(style);
                    //第4列
                    cell = row.createCell(3);
                    cell.setCellValue(jo.getString("current_pay"));
                    cell.setCellStyle(style);
                    //第5列
                    cell = row.createCell(4);
                    cell.setCellValue(jo.getString("current_rest"));
                    cell.setCellStyle(style);
                }

                wb.write(out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(false, "系统问题：导出错误");
            }
        }
        return new Result(true, "导出成功");
    }

}
