package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/booking")
    public String booking(Model model, @RequestParam("bookingId") int bookingId/*, Principal principal*/){
       // fjerner denne så man ikke skal bruge en godkendelse endnu.
        //String tattooArtistId = principal.getName();
        //Hardcodet artist username
        String tattooArtistId = "bigDummy";
        model.addAttribute("booking", bookingService.showBooking(bookingId, tattooArtistId));
        return "home/booking";
    }


}
