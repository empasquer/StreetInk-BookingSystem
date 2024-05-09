package com.example.streetinkbookingsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LogInController {

    @GetMapping("/login")
    public String LogIn(@RequestParam("username") String username){

        return "home/login";
    }
}
