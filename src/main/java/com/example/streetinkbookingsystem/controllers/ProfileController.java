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
public class ProfileController {
    @Autowired
    TattooArtistService tattooArtistService;
    @Autowired
    LoginService loginService;

    @GetMapping("/profile")
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
            return "home/profile";
        }

    }


    @GetMapping("/create-new-profile")
    public String createNewProfile(){

        return "home/create-new-profile";
    }


    @GetMapping("/manage-profiles")
        public String manageProfiles(Model model, HttpSession session){
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.isAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        List<TattooArtist> profiles = tattooArtistService.showTattooArtist();
        model.addAttribute("profiles", profiles);
        model.addAttribute("user", tattooArtist);
        return "home/manage-profiles";
        }

}
