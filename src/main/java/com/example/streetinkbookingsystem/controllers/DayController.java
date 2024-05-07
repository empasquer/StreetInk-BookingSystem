package com.example.streetinkbookingsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DayController {

    @GetMapping("/day")
    public String seeDay(){
        return "home/day";
    }
}
