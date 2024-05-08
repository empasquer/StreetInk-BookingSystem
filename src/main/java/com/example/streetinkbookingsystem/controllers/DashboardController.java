package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String seeDashboard(Model model) {

        // temporary profile
        TattooArtist profile = new TattooArtist();
        profile.setUsername("EmTheBest");


        model.addAttribute("profile", profile);
        return "home/dashboard";
    }
}
