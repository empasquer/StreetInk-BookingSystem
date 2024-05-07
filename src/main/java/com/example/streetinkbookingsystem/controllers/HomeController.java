package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.services.TattooArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private TattooArtistService tattooArtistService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("tattooArtist", tattooArtistService.showTattooArtist());
        return "home/index";
    }
}
