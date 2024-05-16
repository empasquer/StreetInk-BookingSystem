package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
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
