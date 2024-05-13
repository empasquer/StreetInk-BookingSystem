package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/booking")
    public String booking(Model model, @RequestParam int bookingId, @RequestParam String username){
        if (username == null){
            // Redirect logic when username is null
            return "redirect:/";
        }
        model.addAttribute("booking", bookingService.getBookingDetail(bookingId));
        return "home/booking";
    }


    @GetMapping("/create-new-booking")
        public String createNewBooking(Model model, @RequestParam LocalDate date){
            model.addAttribute("date",date);
            return "home/create-new-booking";
        }


}
