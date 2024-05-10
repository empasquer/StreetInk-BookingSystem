package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DayController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/day")
    public String seeDay(Model model, @RequestParam LocalDate date, @RequestParam String username){
        List<Booking> bookingList = bookingService.getBookingsForDay(date,username);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("date", date);
        return "home/day";
    }

    // note: In summary, use addFlashAttribute when you need to pass data between requests,
    // especially during redirects, and you want the data to be available only for the next
    // request. Use addAttribute when you want to pass data to the view for rendering during
    // the current request.
}
