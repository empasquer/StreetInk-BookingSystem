package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    LoginService loginService;

    @Autowired
    TattooArtistService tattooArtistService;

    @GetMapping("/create-new-profile")
    public String createNewProfile(){

        return "home/create-new-profile";
    }

    @GetMapping("/reset-password")
    public String resetPassword() {
        return "home/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String currentPassword, @RequestParam String newPassword,
                                @RequestParam String repeatedPassword, HttpSession session, Model model,
                                RedirectAttributes redirectAttributes) {
        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (loggedIn) {
            model.addAttribute("loggedIn", loggedIn);
        } else {
            return "redirect:/";
        }

        String username = (String) session.getAttribute("username");
        String hashedPassword = tattooArtistService.getPassword(username);

        if (loginService.verifyPassword(currentPassword, hashedPassword)) {
            if (newPassword.equals(repeatedPassword)) {
                loginService.updatePassword(newPassword, username);
            } else {
                redirectAttributes.addFlashAttribute("message", "The two passwords don't match");
                return "redirect:/reset-password";
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Incorrect current password");
            return "redirect:/reset-password";
        }

        return "redirect:/login";
    }


}
