package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ClientController {
    @Autowired
    TattooArtistService tattooArtistService;
    @Autowired
    LoginService loginService;

    @GetMapping("/client?clientId")
    public String seeProfile(HttpSession session, Model model){

        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (loggedIn) {
            model.addAttribute("loggedIn", loggedIn);
        } else {
            return "redirect:/";
        }
        model.addAttribute("loggedIn", loggedIn);


        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);

        model.addAttribute("username", tattooArtist.getUsername());
        model.addAttribute("tattooArtist", tattooArtist);


        if (username == null){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        } else {
            return "redirect:/client?clientId=" + "&year="  + previousDate.getYear() + "&month=" + previousDate.getMonthValue();
        }

    }

}
