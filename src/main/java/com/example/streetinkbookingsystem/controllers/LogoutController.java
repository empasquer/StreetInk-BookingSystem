package com.example.streetinkbookingsystem.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class LogoutController {

    /**
     * @author Munazzah
     * @param request Used to get the session so we can invalidate it
     * @param sessionStatus Status of current session
     * @return Redirects to index page
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, SessionStatus sessionStatus) {
        request.getSession().invalidate();
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
