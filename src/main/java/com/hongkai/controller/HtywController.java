package com.hongkai.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.hongkai.code.Result;
import com.hongkai.domain.Customer;
import com.hongkai.domain.Money;
import com.hongkai.domain.Record;
import com.hongkai.service.CustomerService;
import com.hongkai.service.LuruService;
import com.hongkai.service.MoneyService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.DateUtil;

import static util.Constants.HTYW;
import static util.Constants.ZXYW;
import static util.Constants.getRequestParams;

/**
 * @author huiliu
 * @date 17/10/29
 */
@Controller
@RequestMapping(value = "/htyw")
@Slf4j
public class HtywController {
    @Autowired
    LuruService luruService;

    @Autowired
    VarietyService varietyService;

    @Autowired
    CustomerService customerService;

    @Autowired
    MoneyService moneyService;

    @RequestMapping(value = "/lr_page")
    public String lrPage(ModelMap model) {
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/htyw/lr_page";
    }

    @RequestMapping(value = "/yskmx_page")
    public String yskmxPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", todayDate);
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/htyw/yskmx_page";
    }

    @RequestMapping(value = "/mx_page")
    public String mxPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        Result varietyResult = varietyService.getVarietyList();
        model.addAttribute("varietyResult", varietyResult);
        model.addAttribute("today_date", todayDate);
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/htyw/mx_page";
    }

    @RequestMapping(value = "/khpzhz_page")
    public String khpzhzPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/htyw/khpzhz_page";
    }

    @RequestMapping(value = "/pzhz_page")
    public String pzhzPage(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        return "/htyw/pzhz_page";
    }

    /**
     * 应收款页面
     *
     * @return
     */
    @RequestMapping(value = "/ysk_page")
    public String yskPage(ModelMap model) {
        model.addAttribute("today_date", DateUtil.getTodayDate());
        return "/htyw/ysk_page";
    }


    /**
     * 应收款明细查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/ysk_detail")
    public Result yskDetail(
        @RequestParam("customer") String customer,
        @RequestParam("customer_id") Integer customer_id,
        @RequestParam("start_date") String startDate,
        @RequestParam("end_date") String endDate) {
        /**
         * 没填用户信息
         */
        if(customer_id == null && StringUtils.isEmpty(customer)){
            return moneyService.allMoneysByType(HTYW, startDate, endDate);
        }

        /**
         * 有用户id
         */
        if(customer_id != null){
            return moneyService.allMoneysByTypeAndCustomer(customer_id,HTYW,startDate,endDate);
        }

        /**
         * 无用户id，有用户名字
         */
        if(!StringUtils.isEmpty(customer)){
            Customer c = customerService.getCustomerByName(customer);
            if(c != null){
                return moneyService.allMoneysByTypeAndCustomer(c.getId(),HTYW,startDate,endDate);
            }else{
                return new Result(Lists.newArrayList());
            }
        }

        return new Result(Lists.newArrayList());
    }


    public static List<String> YSK_TABLE_HEADER = Lists.newArrayList("客户名称", "日期", "本期付款金额", "优惠金额");

    @ResponseBody
    @RequestMapping(value = "/ysk_detail_excel")
    public Result yskMxExcel(HttpServletRequest request, HttpServletResponse response) {
        Map map = getRequestParams(request);
        String customer = MapUtils.getString(map, "customer");
        Integer customerId = MapUtils.getInteger(map, "customer_id");
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result = yskDetail(customer,customerId,startDate,endDate);
        if (result.isSuccess()) {
            List<Money> moneys = (List<Money>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = "合同业务应收款明细" + "_" + startDate + "_" + endDate;
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
                for (int j = 0; j < moneys.size(); j++) {
                    Money money = moneys.get(j);
                    row = sheet.createRow(j + 1);
                    //第1列
                    cell = row.createCell(0);
                    cell.setCellValue(money.getCustomer());
                    cell.setCellStyle(style);
                    //第2列
                    cell = row.createCell(1);
                    cell.setCellValue(money.getDate());
                    cell.setCellStyle(style);
                    //第3列
                    cell = row.createCell(2);
                    cell.setCellValue(money.getNum());
                    cell.setCellStyle(style);
                    //第4列
                    cell = row.createCell(3);
                    cell.setCellValue(money.getDiscount());
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
     * 个人明细查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail")
    public Result detail(@RequestParam("start_date") String startDate,
                         @RequestParam("end_date") String endDate) {
        return luruService.allRecordsByType(HTYW, startDate, endDate);
    }

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

        Result result = luruService.allRecordsByType(HTYW, startDate, endDate);
        if (result.isSuccess()) {
            List<Record> records = (List<Record>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = "合同业务明细" + "_" + startDate + "_" + endDate;
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
     * 个人明细查询数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/gr_detail")
    public Result detail(
        @RequestParam("customer_id") Integer customerId,
        @RequestParam("start_date") String startDate,
        @RequestParam("end_date") String endDate) {
        return luruService.allRecordsByCustomerAndType(customerId, HTYW, startDate, endDate);
    }

    @ResponseBody
    @RequestMapping(value = "/gr_detail_excel")
    public Result grMxExcel(HttpServletRequest request, HttpServletResponse response) {
        Map map = getRequestParams(request);
        String customer = MapUtils.getString(map, "customer");
        Integer customerId = MapUtils.getInteger(map, "customer_id");
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result = luruService.allRecordsByCustomerAndType(customerId, HTYW, startDate, endDate);
        if (result.isSuccess()) {
            List<Record> records = (List<Record>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = customer + "合同业务明细" + "_" + startDate + "_" + endDate;
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

    @ResponseBody
    @RequestMapping(value = "/khpzhz")
    public Result khpzhz(@RequestParam("start_date") String startDate,
                         @RequestParam("end_date") String endDate) {
        return luruService.sumRecordByVarietyAndCustomerAndType(HTYW, startDate, endDate);
    }

    public static List<String> PZKHHZ_TABLE_HEADER = Lists.newArrayList("客户名称", "品种", "数量", "运费", "总额");

    @ResponseBody
    @RequestMapping(value = "/khpzhz_excel")
    public Result qbhzExcel(HttpServletRequest request, HttpServletResponse response) {
        Map map = getRequestParams(request);
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result = luruService.sumRecordByVarietyAndCustomerAndType(HTYW, startDate, endDate);
        if (result.isSuccess()) {
            List<Record> records = (List<Record>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = "合同业务分客户分品种汇总" + "_" + startDate + "_" + endDate;
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
                for (int i = 0; i < PZKHHZ_TABLE_HEADER.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(PZKHHZ_TABLE_HEADER.get(i));
                    cell.setCellStyle(headStyle);
                    //设置列表宽度
                    sheet.setColumnWidth(i, PZKHHZ_TABLE_HEADER.get(i).toString().length() * 800);
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
                    cell.setCellValue(record.getCarFee());
                    cell.setCellStyle(style);
                    //第5列
                    cell = row.createCell(4);
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

    @ResponseBody
    @RequestMapping(value = "/gr_pzhz")
    public Result grPzhz(@RequestParam("customer_id") Integer customerId,
                         @RequestParam("start_date") String startDate,
                         @RequestParam("end_date") String endDate) {
        return luruService.sumRecordByVarietyAndTypeForCustomer(customerId, HTYW, startDate, endDate);
    }

    public static List<String> GR_PZHZ_TABLE_HEADER = Lists.newArrayList("客户名称", "品种", "数量", "运费", "总额");
    public static List<String> QBHZ_TABLE_HEADER = Lists.newArrayList("品种", "数量", "运费", "总额");

    @ResponseBody
    @RequestMapping(value = "/gr_pzhz_excel", method = RequestMethod.POST)
    public Result grPzhzExcel(HttpServletRequest request, HttpServletResponse response) {
        Map map = getRequestParams(request);
        String customer = MapUtils.getString(map, "customer");
        Integer customerId = MapUtils.getInteger(map, "customer_id");
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result = luruService.sumRecordByVarietyAndTypeForCustomer(customerId, HTYW, startDate, endDate);
        if (result.isSuccess()) {
            List<Record> records = (List<Record>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = customer + "分品种汇总" + "_" + startDate + "_" + endDate;
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
                for (int i = 0; i < GR_PZHZ_TABLE_HEADER.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(GR_PZHZ_TABLE_HEADER.get(i));
                    cell.setCellStyle(headStyle);
                    //设置列表宽度
                    sheet.setColumnWidth(i, GR_PZHZ_TABLE_HEADER.get(i).toString().length() * 800);
                }
                //为Excel添加数据
                for (int j = 0; j < records.size(); j++) {
                    Record record = records.get(j);
                    row = sheet.createRow(j + 1);
                    //第1列
                    cell = row.createCell(0);
                    cell.setCellValue(record.getCustomer());
                    cell.setCellStyle(style);
                    //第1列
                    cell = row.createCell(1);
                    cell.setCellValue(record.getVariety());
                    cell.setCellStyle(style);
                    //第2列
                    cell = row.createCell(2);
                    cell.setCellValue(record.getNum());
                    cell.setCellStyle(style);
                    //第3列
                    cell = row.createCell(3);
                    cell.setCellValue(record.getCarFee());
                    cell.setCellStyle(style);
                    //第4列
                    cell = row.createCell(4);
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

    @ResponseBody
    @RequestMapping(value = "/pzhz")
    public Result pzhz(@RequestParam("start_date") String startDate,
                       @RequestParam("end_date") String endDate) {
        return luruService.sumRecordByVarietyAndType(HTYW, startDate, endDate);
    }

    @ResponseBody
    @RequestMapping(value = "/pzhz_excel", method = RequestMethod.POST)
    public Result pzhzExcel(HttpServletRequest request, HttpServletResponse response) {
        Map map = getRequestParams(request);
        String startDate = MapUtils.getString(map, "start_date");
        String endDate = MapUtils.getString(map, "end_date");

        if (StringUtils.isEmpty(startDate)) {
            return new Result(false, "开始日期不能为空");
        }
        if (StringUtils.isEmpty(endDate)) {
            return new Result(false, "截止日期不能为空");
        }

        Result result = luruService.sumRecordByVarietyAndType(HTYW, startDate, endDate);
        if (result.isSuccess()) {
            List<Record> records = (List<Record>)result.getResult();
            try {
                //设置输出流
                OutputStream out = response.getOutputStream();
                response.reset();
                // 文件名
                String fileName = "分品种汇总" + "_" + startDate + "_" + endDate;
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
                for (int i = 0; i < QBHZ_TABLE_HEADER.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(QBHZ_TABLE_HEADER.get(i));
                    cell.setCellStyle(headStyle);
                    //设置列表宽度
                    sheet.setColumnWidth(i, QBHZ_TABLE_HEADER.get(i).toString().length() * 800);
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

    /**
     * 获取所有客户列表
     *
     * @return
     */
    @RequestMapping(value = "/grhz_page")
    public String allCustomers(ModelMap model) {
        String todayDate = DateUtil.getTodayDate();
        model.put("start_date", todayDate);
        model.put("end_date", todayDate);
        model.put("result", customerService.getCustomersByType(HTYW));
        return "/htyw/grhz_page";
    }

    @ResponseBody
    @RequestMapping(value = "/ysk_add", method = RequestMethod.POST)
    public Result yskgl_add(@RequestBody Money money) {
        if (money.getCustomerId() == null) {
            Result result = customerService.addCustomer(money.getCustomer());
            if (!result.isSuccess()) {
                return result;
            } else {
                Customer customer = (Customer)result.getResult();
                money.setCustomerId(customer.getId());
            }
        }
        return luruService.addMoney(money);
    }
}
