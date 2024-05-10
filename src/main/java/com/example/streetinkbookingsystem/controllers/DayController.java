package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DayController {

    @Autowired
    private BookingService bookingService;


    @GetMapping("/day")
    public String seeDay(Model model){
        List<Booking> bookingList = bookingService.showBookingList();
        model.addAttribute("bookingList", bookingList);
        return "home/day";
    }


}
