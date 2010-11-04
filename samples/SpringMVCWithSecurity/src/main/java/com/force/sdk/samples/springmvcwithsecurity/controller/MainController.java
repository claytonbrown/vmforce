package com.force.sdk.samples.springmvcwithsecurity.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.force.sdk.samples.springmvcwithsecurity.model.MyFirstCloudEntity;
import com.force.sdk.samples.springmvcwithsecurity.service.CloudService;

/**
 * @author fhossain@salesforce.com
 */

@Controller
public class MainController {

    private CloudService cloudService;
    
    @Autowired
    public MainController(CloudService cloudService) {
        this.cloudService = cloudService;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex() {
        return "/WEB-INF/jsp/index.jsp";
    }
    
    @RequestMapping(value = "/logout_success.html", method = RequestMethod.GET)
    public String logoutSuccess() {
        return "/WEB-INF/jsp/logout_success.jsp";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView postAccount(@RequestParam("name") String name, @RequestParam("number") int number) {
        MyFirstCloudEntity acct = cloudService.createCloudEntity(name, number);
        Map<String, Object> myModel = new HashMap<String, Object>();
        myModel.put("entity", acct);
        return new ModelAndView("/WEB-INF/jsp/create_result.jsp", "model", myModel);
    }
}
