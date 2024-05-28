package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private TattooArtistService tattooArtistService;
    @Autowired
    private BookingService bookingService;


    /**
     * @author Nanna
     * @param year used to create a localDate
     * @param month used to create a LocalDate
     * @return all LocalDates in a specific month
     */
    public ArrayList<LocalDate> getDaysInMonth(int year, Month month) {
        ArrayList<LocalDate> daysInMonth = new ArrayList<>();
        LocalDate firstDate = LocalDate.of(year, month, 1); // Start from the first day of the month

        while (firstDate.getMonth() == month) {
            daysInMonth.add(firstDate); // Add the current date to the list
            firstDate = firstDate.plusDays(1); // Move to the next day
        }
        return daysInMonth;
    }

    /**
     * @author Nanna
     * @param firstDateInMonth used to determine the weekNumber of its week
     * @return an Array of 6 weekNumbers starting at the first date then the next 5 weeks.
     */
    public  int[] getWeekNumbers(LocalDate firstDateInMonth){
         int[] weekNumbers = new int[6];
        int weekNumber = firstDateInMonth.get(WeekFields.ISO.weekOfWeekBasedYear());
        for (int i=0; i< 6; i++  ){
            weekNumbers[i]= weekNumber;
            weekNumber +=1;
        }
        return weekNumbers;
    }

    /**
     * @author Nanna
     * @param firstDateInMonth used to determine which weekday the first date is
     * @return the numbers of empty startFills: number weekdays before the first date in the month
     */
    public int getEmptyStartFills(LocalDate firstDateInMonth) {
        int firstDay = firstDateInMonth.getDayOfWeek().getValue();
        int fillers = firstDay - 1; // Subtract 1 because we want Monday to have 0 fillers
        return fillers;
    }

    /**
     * @author Nanna
     * @param firstDateInMonth used to determine number of startFills
     * @param daysInMonth used to determine how many empty cells left in the
     *                   calendar after the last date.
     * @return number of empty cells after startFills + daysInMonth based on the 6x7 calendar grid
     */
    public int getEmptyEndFills(LocalDate firstDateInMonth, ArrayList daysInMonth) {
        int startFillers = getEmptyStartFills(firstDateInMonth);
        int endFillers = 42 - startFillers - daysInMonth.size();
        return endFillers;

    }

    /**
     * @author Nanna
     * @param currentDate used to get number of bookings for the date
     * @param username used to get bookings.
     * @return how many hours of the artist's day is booked in percentage, based on the artist's  number of
     * work hours and the total duration of all bookings on the day.
     */
    public int calculateDegreeOfBookedDay(LocalDate currentDate, String username){
        TattooArtist artist = tattooArtistService.getTattooArtistByUsername(username);
        double avgWorkHours = artist.getAvgWorkHours();
        List<Booking> bookingsOnDate = bookingService.getBookingsForDay(currentDate, username);
        int totalBookedHoursForDay = dashboardService.calculateTotalBookedHours(bookingsOnDate);
        double bookingPercentage = ((double) totalBookedHoursForDay / avgWorkHours) * 100;
        return (int) Math.round(bookingPercentage);
    }




}

