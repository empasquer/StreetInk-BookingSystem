package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Service
public class DashboardService {
    @Autowired BookingService bookingService;
    @Autowired TattooArtistService tattooArtistService;

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
        //CHAT GAVE ME THIS

        // Get the WeekFields for ISO definition (Monday as the start of the week, week starting on Monday)
        WeekFields weekFields = WeekFields.ISO;

        // Get the week number using ISO week-of-week-based-year
        int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        return weekNumber;
    }


    public int calculateBookingPercentageOfMonth(String username) {
        // Find month how many days in month and how many bookings in the month for the username
        LocalDate currentDate = LocalDate.now();
        int totalDaysInMonth = currentDate.lengthOfMonth();
        int bookingsForMonth = bookingService.getBookingCountForMonth(
                currentDate.getYear(),
                currentDate.getMonthValue(),
                username);

        // Find usernames avg work hours a day and make find an avergare monthly bookings available
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        double averageWorkHoursPerDay = tattooArtist.getAvgWorkHours();
        double expectedBookingsForMonth = averageWorkHoursPerDay * totalDaysInMonth;
        // find percentage of bookings
        // -> (bookings in a month / average bookings available a month) * 100
        double bookingPercentage = (bookingsForMonth / expectedBookingsForMonth) * 100;
        System.out.println((int) Math.round(bookingPercentage));
        return (int) Math.round(bookingPercentage);
    }

    public int calculateMonthProgressPercentage() {
        LocalDate currentDate = LocalDate.now();
        int totalDaysInMonth = currentDate.lengthOfMonth();
        int currentDayOfMonth = currentDate.getDayOfMonth();
        // progress of onth = (current day / total days of the month ) * 100
        double progressPercentage = ((double) currentDayOfMonth / totalDaysInMonth) * 100;
        System.out.println((int) Math.round(progressPercentage));
        return (int) Math.round(progressPercentage);
    }

}
