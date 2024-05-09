package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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
        LocalDate currentDate = LocalDate.now();
        int totalDaysInMonth = 0;
        int bookingsForMonth = 0;
        int weekdaysInMonth = 0;

        // Calculate total days in the month excluding weekends (saturday and sunday)
        // DayOfWeek er en java klasse

        // loop alle dagene i en givene måned, altså det her måned og ikke tælle saturday og sunday med
        for (int day = 1; day <= currentDate.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), day);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                weekdaysInMonth++;
            }
        }

        // Get the total bookings for the month for the given username
        bookingsForMonth = bookingService.getBookingCountForMonth(currentDate.getYear(), currentDate.getMonthValue(), username);

        // Find the usernames' average work hours per day and calculate the expected bookings for the month
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        double averageWorkHoursPerDay = tattooArtist.getAvgWorkHours();
        double expectedBookingsForMonth = averageWorkHoursPerDay * weekdaysInMonth;

        // Calculate the booking percentage
        double bookingPercentage = (bookingsForMonth / expectedBookingsForMonth) * 100;

        return (int) Math.round(bookingPercentage);
    }

    public int calculateMonthProgressPercentage() {
        LocalDate currentDate = LocalDate.now();
        int totalDaysInMonth = 0;
        int currentDayOfMonth = currentDate.getDayOfMonth();
        int weekdaysInMonth = 0;

        // same as before, calc days in month without counting weekends
        for (int day = 1; day <= currentDate.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), day);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                weekdaysInMonth++;
            }
        }

        // Calculate the progress percentage
        double progressPercentage = ((double) currentDayOfMonth / weekdaysInMonth) * 100;

        return (int) Math.round(progressPercentage);
    }

}
