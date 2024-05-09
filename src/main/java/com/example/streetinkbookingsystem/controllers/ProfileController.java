package com.example.streetinkbookingsystem.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/create-new-profile")
    public String createNewProfile(){

        return "home/create-new-profile";
    }
}
