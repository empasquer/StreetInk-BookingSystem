package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.BookingService;
import com.example.streetinkbookingsystem.services.DashboardService;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class DashboardController {

    @Autowired
    TattooArtistService tattooArtistService;

    @Autowired
    DashboardService dashboardService;

    @Autowired
    LoginService loginService;


    /**
     * Displays the dashboard view for the logged-in user.
     * <p>
     * This method checks if the user is logged in, retrieves the relevant data
     * for the dashboard, and adds it to the model. If the user is not logged in,
     * they are redirected to the home page.
     * </p>
     *
     * @param model   the model to add attributes to for rendering view
     * @param session the current HTTP session
     * @return the name of the view to be rendered, or a redirect to login if not logged in
     */

    @GetMapping("/dashboard")
    public String seeDashboard(Model model, HttpSession session) {
        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (loggedIn) {
            model.addAttribute("loggedIn", loggedIn);
        } else {
            return "redirect:/";
        }

        String username = (String) session.getAttribute("username");

        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);

        int bookingsADay = dashboardService.calculateAmtBookingsADay(tattooArtist.getUsername());
        int bookingsAWeek = dashboardService.calculateAmtBookingsAWeek(tattooArtist.getUsername());

        int bookingPercentageOfMonth = dashboardService.calculateBookingPercentageOfMonth(tattooArtist.getUsername());
        int monthProgressPercentage = dashboardService.calculateMonthProgressPercentage();

        model.addAttribute("tattooArtist", tattooArtist);
        model.addAttribute("bookingsADay", bookingsADay);
        model.addAttribute("bookingsAWeek", bookingsAWeek);
        model.addAttribute("bookingPercentageOfMonth", bookingPercentageOfMonth);
        model.addAttribute("monthProgressPercentage", monthProgressPercentage);
        return "home/dashboard";
    }
}
