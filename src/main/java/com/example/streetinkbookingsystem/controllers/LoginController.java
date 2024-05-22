package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.services.EmailService;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    EmailService emailService;

    @Autowired
    TattooArtistService tattooArtistService;


    @GetMapping("/login")
    public String login() {
        // loginService.hashExistingPasswords();
        return "home/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        if (loginService.authenticateUser(username, password)){
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid username or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/forgotten-password")
    public String forgottenPassword() {
        return "home/forgotten-password";
    }

    @PostMapping("/forgotten-password")
    public String forgottenPassword(@RequestParam String email, @RequestParam String username, RedirectAttributes redirectAttributes) {
        if (tattooArtistService.getTattooArtistByUsername(username) == null) {
            redirectAttributes.addFlashAttribute("message", "Invalid username");
            return "redirect:/forgotten-password";
        }

        if(!emailService.isValidEmail(email, username)) {
            redirectAttributes.addFlashAttribute("message", "Invalid email. Use the one saved in your profile");
            return "redirect:/forgotten-password";
        }

        String newPassword = loginService.randomPassword();
        String mailContent = "Here is your new password: " + newPassword;
        emailService.sendEmail(email, "New Password", mailContent);
        loginService.updatePassword(newPassword, username);

        return "redirect:/login";
    }

}