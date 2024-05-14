package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.BookingService;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
public class DayController {

    @Autowired
    BookingService bookingService;
    @Autowired
    LoginService loginService;
    @Autowired
    TattooArtistService tattooArtistService;


    @GetMapping("/day")
    public String seeDay(Model model, HttpSession session, @RequestParam LocalDate date, @RequestParam(required = false) String username){
        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (loggedIn) {
            model.addAttribute("loggedIn", loggedIn);
        } else {
            return "redirect:/";
        }

        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        if (username == null){
            // Redirect logic when username is null
            return "redirect:/";
        }



        List<Booking> bookingList = bookingService.getBookingsForDay(date,username);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("date", date);
        model.addAttribute("username", username);

        // might move to service, this makes a list of quarter hours
        List<Double> hours = new ArrayList<>();
        for (double hour = 9; hour <= 20; hour += 0.25) {
            hours.add(hour);
        }
        model.addAttribute("hours", hours);
        model.addAttribute("bookingService",bookingService);

        return "home/day";
    }

    @PostMapping("/day/next")
    public String seeNextDay( @RequestParam LocalDate date, @RequestParam String username) {
        LocalDate nextDate = date.plusDays(1);
        return "redirect:/day?username=" + username + "&date=" + nextDate;
    }

    @PostMapping("/day/previous")
    public String seePreviousDay( @RequestParam LocalDate date,@RequestParam String username) {
        LocalDate previousDate = date.minusDays(1);
        return "redirect:/day?username=" + username + "&date=" + previousDate;
    }


}
