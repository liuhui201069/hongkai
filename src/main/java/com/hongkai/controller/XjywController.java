package com.hongkai.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.hongkai.code.Result;
import com.hongkai.domain.Record;
import com.hongkai.service.LuruService;
import com.hongkai.service.VarietyService;
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

import static util.Constants.XJYW;
import static util.Constants.getRequestParams;

/**
 * @author huiliu
 * @date 17/10/29
 */
@Controller
@RequestMapping(value = "/xjyw")
@Slf4j
public class XjywController {
    @Autowired
    LuruService luruService;

    @Autowired
    VarietyService varietyService;

    @RequestMapping(value = "/lr_page")
    public String lrPage(ModelMap model) {
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/xjyw/lr_page";
    }

    @RequestMapping(value = "/mx_page")
    public String mxPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", todayDate);
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/xjyw/mx_page";
    }

    @RequestMapping(value = "/hz_page")
    public String hzPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/xjyw/hz_page";
    }

    /**
     * 个人明细查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail")
    public Result sum(@RequestParam("start_date") String startDate,
                      @RequestParam("end_date") String endDate) {
        return luruService.allRecordsByType(XJYW, startDate, endDate);
    }

    /**
     * 个人明细查询数据
     *
     * @return
     */
    public static List<String> MX_TABLE_HEADER = Lists.newArrayList("客户名称", "品种", "数量", "单价", "单位", "运费", "总额", "车号",
        "承运人", "时间");

    @ResponseBody
    @RequestMapping(value = "/detail_excel")
    public Result mxExcel(HttpServletRequest request, HttpServletResponse response) {
        Map map = getRequestParams(request);
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result = luruService.allRecordsByType(XJYW, startDate, endDate);
        if (result.isSuccess()) {
            List<Record> records = (List<Record>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = "现金业务明细" + "_" + startDate + "_" + endDate;
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
                for (int i = 0; i < MX_TABLE_HEADER.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(MX_TABLE_HEADER.get(i));
                    cell.setCellStyle(headStyle);
                    //设置列表宽度
                    sheet.setColumnWidth(i, MX_TABLE_HEADER.get(i).toString().length() * 800);
                }
                //为Excel添加数据
                for (int j = 0; j < records.size(); j++) {
                    Record record = records.get(j);
                    row = sheet.createRow(j + 1);
                    //第1列
                    cell = row.createCell(0);
                    cell.setCellValue(record.getCustomer());
                    cell.setCellStyle(style);
                    //第2列
                    cell = row.createCell(1);
                    cell.setCellValue(record.getVariety());
                    cell.setCellStyle(style);
                    //第3列
                    cell = row.createCell(2);
                    cell.setCellValue(record.getNum());
                    cell.setCellStyle(style);
                    //第4列
                    cell = row.createCell(3);
                    cell.setCellValue(record.getPrice());
                    cell.setCellStyle(style);
                    //第5列
                    cell = row.createCell(4);
                    cell.setCellValue(record.getUnit());
                    cell.setCellStyle(style);
                    //第6列
                    cell = row.createCell(5);
                    cell.setCellValue(record.getCarFee());
                    cell.setCellStyle(style);
                    //第7列
                    cell = row.createCell(6);
                    cell.setCellValue(record.getTotal());
                    cell.setCellStyle(style);
                    //第8列
                    cell = row.createCell(7);
                    cell.setCellValue(record.getCarNumber());
                    cell.setCellStyle(style);
                    //第9列
                    cell = row.createCell(8);
                    cell.setCellValue(record.getCarOwner());
                    cell.setCellStyle(style);
                    //第10列
                    cell = row.createCell(9);
                    cell.setCellValue(record.getDate());
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

    /**
     * 按现金业务的查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/hz")
    public Result xjywSum(@RequestParam("start_date") String startDate,
                          @RequestParam("end_date") String endDate) {
        return luruService.sumRecordByVarietyAndType(XJYW, startDate, endDate);
    }

    @ResponseBody
    @RequestMapping(value = "/hz_excel", method = RequestMethod.POST)
    public Result xjywPzHzExcel(HttpServletRequest request, HttpServletResponse response) {
        String filename = "现金业务汇总";
        return getXjywSumExcel(request, response, filename);
    }

    public static List<String> XJYW_TABLE_HEADER = Lists.newArrayList("品种", "数量", "运费", "总额");

    private Result getXjywSumExcel(HttpServletRequest request, HttpServletResponse response, String filename) {
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



}
