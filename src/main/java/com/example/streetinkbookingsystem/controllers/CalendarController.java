package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.services.BookingService;
import com.example.streetinkbookingsystem.services.CalendarService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
public class CalendarController {

    @Autowired
    CalendarService calendarService;
    @Autowired
    BookingService bookingService;
    @Autowired
    TattooArtistService tattooArtistService;

    // If there is passed date in the parameters then it display that month/year.
    // otherwise it will display the current month.
    @GetMapping("/calendar")
    public String seeCurrentMonth(Model model, @RequestParam(required = false) Integer year, @RequestParam(required = false)  Integer month) {


        // DUMMY USERNAME - skal ændres til den rigtig username
        String username = tattooArtistService.showTattooArtist().get(0).getUsername();
        model.addAttribute("username", username);


        LocalDate date;
        if (year == null && month == null) {
             date = calendarService.getCurrentDate();
             // HAR ÆNDRET HER FORDI METODEN I BOOKINGSERVICE OG REPO ER BLEVET ÆNDRET
            System.out.println(bookingService.getBookingCountForDate(date, username));
        } else
             date= LocalDate.of(year, month, 1); // start day is always the first in the month
        ArrayList<LocalDate> daysInMonth = calendarService.getDaysInMonth(date.getYear(), date.getMonth());
        int[] weekNumbers = calendarService.getWeekNumbers(daysInMonth.get(0)); // calculate the week numbers based on the first date in the month
       int startFillers = calendarService.getEmptyStartFills(daysInMonth.get(0));
       int endFillers = calendarService.getEmptyEndFills(daysInMonth.get(0),daysInMonth);
        model.addAttribute("startFillers", startFillers);
        model.addAttribute("endFillers", endFillers);
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("date", date);
        model.addAttribute("bookingService", bookingService);
        model.addAttribute("weekNumbers", weekNumbers);
        model.addAttribute("username", "bigDummy");

        return "home/calendar";
    }


    // will change the month to the month and year given in the params.
  /* @PostMapping("/calender")
    public String seeMonth(Model model, @RequestParam Integer year, @RequestParam Integer month) {
        LocalDate date= LocalDate.of(year, month, 1); // start day i always the first in the month

        ArrayList<LocalDate> daysInMonth = calendarService.getDaysInMonth(date.getYear(), date.getMonthValue());
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("date", date);
        return "home/calender";
    }

   */

    //finds the next month based on the month and year given
    //query parameters: passed along to the get mapping and displayed in the URL.
    // ? is the start of the query, $ separates the parameters.
    @PostMapping("/calendar/next")
    public String seeNextMonth( @RequestParam Integer year, @RequestParam Integer month) {
        LocalDate nextDate = LocalDate.of(year, month, 1).plusMonths(1);
        return "redirect:/calendar?year=" + nextDate.getYear() + "&month=" + nextDate.getMonthValue();
    }


    //finds the previous month based on the month and year given
    @PostMapping("/calendar/previous")
    public String seePreviousMonth( @RequestParam Integer year, @RequestParam Integer month) {
        LocalDate previousDate = LocalDate.of(year, month, 1).minusMonths(1);
        return "redirect:/calendar?year=" + previousDate.getYear() + "&month=" + previousDate.getMonthValue();
    }
}
