package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TattooArtistService tattooArtistService;

    @Autowired
    LoginService loginService;

    /**
     *
     * @param model
     * @return home/index
     * @author Tara
     */
    @GetMapping("/")
    public String index(Model model){
        List<TattooArtist> tattooArtists = tattooArtistService.showTattooArtist();
        model.addAttribute("tattooArtists", tattooArtists);
        return "home/index";
    }
}
