package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.services.BookingService;
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


    @GetMapping("/day")
    public String seeDay(Model model, @RequestParam LocalDate date, @RequestParam(required = false) String username){
        if (username == null){
            // Redirect logic when username is null
            return "redirect:/";
        }

        List<Booking> bookingList = bookingService.getBookingsForDay(date,username);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("date", date);
<<<<<<< HEAD
=======
        model.addAttribute("username", username);

        // might move to service, this makes a list of quarter hours
        List<Double> hours = new ArrayList<>();
        for (double hour = 9; hour <= 20; hour += 0.25) {
            hours.add(hour);
        }
        model.addAttribute("hours", hours);
        model.addAttribute("bookingService",bookingService);

>>>>>>> db1b8f5d72bbfcb46789182717b6ed2dd6b52651
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
