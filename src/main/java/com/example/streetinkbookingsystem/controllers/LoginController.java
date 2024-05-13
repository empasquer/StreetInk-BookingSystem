package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.services.LoginService;
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

    @GetMapping("/login")
    public String login() {
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

}

