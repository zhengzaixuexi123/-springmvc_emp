package com.example.controller;

import com.example.DBCPUtils.DBCPUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;

@Controller
public class TestController {

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        Connection con = DBCPUtils.getConnection();
        System.out.println(con);
        return "Test successful";
    }
}