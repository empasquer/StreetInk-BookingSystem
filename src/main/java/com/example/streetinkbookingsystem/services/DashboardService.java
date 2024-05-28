package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private TattooArtistService tattooArtistService;

    /**
     * @author Emma
     * @param username The username of the tattoo artist whose bookings are to be counted.
     * @return The number of bookings for the specified tattoo artist on the current day.
     */
    public int calculateAmtBookingsADay(String username) {
        return bookingService.getBookingCountForDate(LocalDate.now(), username);
    }

    /**
     * @author Emma
     * @param username The username of the tattoo artist whose bookings are to be counted.
     * @return The number of bookings for the specified tattoo artist for the current week.
     */
    public int calculateAmtBookingsAWeek(String username) {
        return bookingService.getBookingCountForThisWeek(username);
    }

    /**
     * @author Emma
     * @param username The username of the tattoo artist whose booking percentage is to be calculated.
     * @return The booking percentage for the specified tattoo artist for the current month, rounded to the nearest integer.
     */
    public int calculateBookingPercentageOfMonth(String username) {
        LocalDate currentDate = LocalDate.now();
        // not counting weekends - so only week days
        int totalWeekdaysInMonth = calculateWeekdaysInMonth(currentDate);
        // havew to get th ebookings for the month
        List<Booking> bookingsForMonth = bookingService.getBookingsForMonth(currentDate.getYear(), currentDate.getMonthValue(), username);
        // the tattoo artist aswell to get his avg amnt of hours/day
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        double averageWorkHoursPerDay = tattooArtist.getAvgWorkHours();
        // from there : total available hours a month based on avg hours a day
        int totalWorkHoursForMonth = (int) Math.round(averageWorkHoursPerDay * totalWeekdaysInMonth);

        // calc how many hours for eahc booking and add them to get a total hours of booked time
        int totalBookedHoursForMonth = calculateTotalBookedHours(bookingsForMonth);


        // now the booking percentage of the available hours of the month
        // based on the avg hours a day
        double bookingPercentage = ((double) totalBookedHoursForMonth / totalWorkHoursForMonth) * 100;
        return (int) Math.round(bookingPercentage);
    }

    /**
     * @author Emma
     * @return The percentage of the current month that has passed, rounded to the nearest integer.
     */
    public int calculateMonthProgressPercentage() {
        LocalDate currentDate = LocalDate.now();
        int currentDayOfMonth = currentDate.getDayOfMonth();
        int totalDaysInMonth = currentDate.lengthOfMonth();

        double progressPercentage = ((double) currentDayOfMonth / totalDaysInMonth) * 100;
        return (int) Math.round(progressPercentage);
    }

    /**
     * @author Emma
     * @return The progress percentage of the current month, rounded to the nearest integer.
     */
    public int calculateWeekdaysInMonth(LocalDate date) {
        int weekdaysInMonth = 0;
        for (int day = 1; day <= date.lengthOfMonth(); day++) {
            LocalDate currentDate = LocalDate.of(date.getYear(), date.getMonthValue(), day);
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                weekdaysInMonth++;
            }
        }
        return weekdaysInMonth;
    }

    /**
     * @author Emma
     * @param bookings A list of bookings for which the total booked hours need to be calculated.
     * @return The total number of booked hours.
     */
    public int calculateTotalBookedHours(List<Booking> bookings) {
        int totalBookedHours = 0;
        for (Booking booking : bookings) {
            LocalTime startTime = booking.getStartTimeSlot();
            LocalTime endTime = booking.getEndTimeSlot();

            // duration between start time and end time -- mega smart Duration class
            Duration duration = Duration.between(startTime, endTime);

            // total hours from duration -- so  smart toHours method
            long hours = duration.toHours();

            // Add total hours to total booked hours
            totalBookedHours += hours;
        }
        return totalBookedHours;
    }
}
