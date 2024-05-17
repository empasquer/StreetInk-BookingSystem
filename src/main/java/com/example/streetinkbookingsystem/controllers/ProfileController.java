package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {
    @Autowired
    TattooArtistService tattooArtistService;


    @GetMapping("/create-new-profile")
    public String newProfile(){
        return "home/create-new-profile";
    }
    @PostMapping("/create-new-profile")
    public String createProfile(Model model, @RequestParam String profileUsername,
                                @RequestParam String profileFirstname,
                                @RequestParam String profileLastName,
                                @RequestParam String profilePassword,
                                @RequestParam String email,
                                @RequestParam int phone,
                                @RequestParam String facebookUrl,
                                @RequestParam String instagramUrl,
                                @RequestParam int avgWorkHours,
                                @RequestParam(value = "isAdmin", required = false) Boolean isAdmin,
                                @RequestParam("profilePicture") MultipartFile profilePicture, HttpSession session) {
        TattooArtist existingProfile = tattooArtistService.getTattooArtistByUsername(profileUsername);


        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";

        }
        if (existingProfile != null) {
            model.addAttribute("message", "This username is taken");
            return "home/create-new-profile"; // Returns the same page with the message
        } else {
            byte[] pictureData = null;
            if (!profilePicture.isEmpty()) {
                try {
                    pictureData = profilePicture.getBytes();
                } catch (IOException e) {
                    return "redirect:/error";
                }
            }

            boolean adminStatus = isAdmin != null && isAdmin;
            tattooArtistService.createProfile(profileUsername, profileFirstname, profileLastName, profilePassword, facebookUrl, instagramUrl, phone, email, avgWorkHours, adminStatus, Optional.ofNullable(pictureData));
            return "redirect:/";
        }
    }


    @GetMapping("/manage-profiles")
        public String manageProfiles(Model model, HttpSession session, @RequestParam(required = false) String profileToDelete, @RequestParam(required = false) String message){
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        if (profileToDelete != null) {
            model.addAttribute("profileToDelete", profileToDelete);
        }

        if (message != null) {
            model.addAttribute("message", message);
        }
        List<TattooArtist> profiles = tattooArtistService.showTattooArtist();
        model.addAttribute("profiles", profiles);
        model.addAttribute("user", tattooArtist);


        return "home/manage-profiles";
        }

    @PostMapping("/manage-profiles")
    public String manageProfilesWithWarning(@RequestParam String profileToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        redirectAttributes.addAttribute("profileToDelete", profileToDelete);
        return "redirect:/manage-profiles";
    }


    @PostMapping("/delete-profile")
    public String deleteProfile(@RequestParam String profileToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        String message = tattooArtistService.deleteProfileByUsername(profileToDelete);
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/manage-profiles";
    }



    @PostMapping("/change-admin")
    public String changeAdmin(@RequestParam String profileToChange, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null || !tattooArtist.getIsAdmin()){
            // Redirect logic when username is null or if not admin.
            return "redirect:/";
        }
        TattooArtist artist = tattooArtistService.getTattooArtistByUsername(profileToChange);
        String message = tattooArtistService.changeAdminStatus(artist);
        redirectAttributes.addAttribute("message", message);
        return "redirect:/manage-profiles";
    }
}


