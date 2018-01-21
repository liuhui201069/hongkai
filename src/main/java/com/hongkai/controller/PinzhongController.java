package com.hongkai.controller;

import com.hongkai.code.Result;
import com.hongkai.domain.Variety;
import com.hongkai.service.VarietyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huiliu
 * @date 17/10/21
 */
@Controller
@RequestMapping(value = "/pz")
@Slf4j
public class PinzhongController {
    @Autowired
    VarietyService varietyService;

    @RequestMapping(value = "/list_page")
    public String list_page(ModelMap model) {
        Result result = varietyService.getVarietyList();
        model.addAttribute("result", result);
        return "/pz/list_page";
    }

    @ResponseBody
    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    public Result list(ModelMap model) {
        return varietyService.getVarietyList();
    }

    @RequestMapping(value = "/add_page")
    public String add_page() {
        return "/pz/add_page";
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Variety variety) {
        log.info("@PinzhongController@ name {}", variety);
        return varietyService.addVariety(variety);
    }

    @RequestMapping(value = "/edit_page")
    public String editPage(ModelMap model) {
        Result result = varietyService.getVarietyList();
        model.addAttribute("result", result);
        return "/pz/edit_page";
    }

    @ResponseBody
    @RequestMapping(value = "/edit")
    public Result edit(@RequestParam("variety") String variety, @RequestParam("newVariety") String newVariety) {
        return varietyService.updateVarietyName(variety, newVariety);
    }
}
