package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.services.BookingService;
import com.example.streetinkbookingsystem.services.CalendarService;
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
public class CalendarController {

    @Autowired
    CalendarService calendarService;
    @Autowired
    BookingService bookingService;
    @Autowired
    TattooArtistService tattooArtistService;
    @Autowired
    LoginService loginService;

    /**
     * @author Nanna
     * @param year not required. If exist then used to build the month
     * @param month not required. If exist then used to build the month
     * @param session Used to determine if the user is logged in or not. User will be redirected
     *               to index page if not logged in.
     * @return calendar view: displays the calendar. If no year or month is passed then display the current month.
     */
    @GetMapping("/calendar")
    public String seeCurrentMonth(Model model, @RequestParam(required = false) Integer year, @RequestParam(required = false)  Integer month, HttpSession session) {
        //Check if user has a session, if not redirect to login.
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        //Initialize the calendar. If client gets to the calendar from another view
        //then show the current month. If they push the "next" or "previous" buttons then show that month.
        LocalDate currentDate = LocalDate.now();
        LocalDate date;
        if (year == null && month == null) {
             date = currentDate;
        } else
             date= LocalDate.of(year, month, 1); // start day is always the first in the month

        // Calculate the days in the month, get the weekNumbers, and calculate how many empty fills
        // there are needed before and after the dates of the months, so that the matrix is always
        // full, 6x7.
        List<Booking> bookingsToday = bookingService.getBookingsForDay(currentDate,(String) session.getAttribute("username"));
        ArrayList<LocalDate> daysInMonth = calendarService.getDaysInMonth(date.getYear(), date.getMonth());
        int[] weekNumbers = calendarService.getWeekNumbers(daysInMonth.get(0)); // calculate the week numbers based on the first date in the month
        int startFillers = calendarService.getEmptyStartFills(daysInMonth.get(0));
        int endFillers = calendarService.getEmptyEndFills(daysInMonth.get(0),daysInMonth);


        //Add to model
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("bookingsToday", bookingsToday);
        model.addAttribute("startFillers", startFillers);
        model.addAttribute("endFillers", endFillers);
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("date", date);
        model.addAttribute("calendar", calendarService); //used in view to calculate how booked the day is
        model.addAttribute("weekNumbers", weekNumbers);
        return "home/calendar";
    }


    /**
     * @author Nanna
     * @param year used so make a LocalDate
     * @param month used to make a LocalDate then added 1 month to show the next month
     * @return calendar view for the next month
     */
    @PostMapping("/calendar/next")
    public String seeNextMonth( @RequestParam Integer year, @RequestParam Integer month) {
        LocalDate nextDate = LocalDate.of(year, month, 1).plusMonths(1);
        return "redirect:/calendar?username=" + "&year=" + nextDate.getYear() + "&month=" + nextDate.getMonthValue();
    }


    /**
     * @author Nanna
     * @param year used so make a LocalDate
     * @param month used to make a LocalDate then subtracted 1 month to show the previous month
     * @return calendar view for the previous  month
     */
    @PostMapping("/calendar/previous")
    public String seePreviousMonth( @RequestParam Integer year, @RequestParam Integer month) {
        LocalDate previousDate = LocalDate.of(year, month, 1).minusMonths(1);
        return "redirect:/calendar?username=" + "&year="  + previousDate.getYear() + "&month=" + previousDate.getMonthValue();
    }
}
