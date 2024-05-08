package com.example.streetinkbookingsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String seeDashboard() {
        return "home/dashboard";
    }
}
