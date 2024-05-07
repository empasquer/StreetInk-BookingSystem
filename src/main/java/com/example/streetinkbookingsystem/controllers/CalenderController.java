package com.example.streetinkbookingsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalenderController {

    @GetMapping("/calender")
    public String seeCalender() {
        return "home/calender";
    }
}
