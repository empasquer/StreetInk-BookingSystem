package com.example.streetinkbookingsystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Service
public class DashboardService {
    @Autowired BookingService bookingService;

    public int calculateAmtBookingsADay(String username) {
        return bookingService.getBookingCountForDate(LocalDate.now(), username);
    }

    public int calculateAmtBookingsAWeek(String username) {
        System.out.println(LocalDate.now().getYear());
        System.out.println(LocalDate.now().getMonthValue());
        System.out.println(getWeekNumber(LocalDate.now()));

        return bookingService.getBookingCountForWeek(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), getWeekNumber(LocalDate.now()), username);
    }

    public static int getWeekNumber(LocalDate date) {
        // Get the WeekFields for ISO definition (Monday as the start of the week, week starting on Monday)
        WeekFields weekFields = WeekFields.ISO;

        // Get the week number using ISO week-of-week-based-year
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        return weekNumber;
    }
}
