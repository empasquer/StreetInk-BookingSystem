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

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/booking")
    public String booking(Model model, @RequestParam int bookingId, @RequestParam String username/*, Principal principal*/){
       // fjerner denne så man ikke skal bruge en godkendelse endnu.
        //String tattooArtistId = principal.getName();
        //Hardcodet artist username
        String tattooArtistId = username;
        model.addAttribute("booking", bookingService.getBookingDetail(bookingId));
        return "home/booking";
    }


    @GetMapping("/create-new-booking")
        public String createNewBooking(Model model, @RequestParam LocalDate date){
            model.addAttribute("date",date);
            return "home/create-new-booking";
        }


}
