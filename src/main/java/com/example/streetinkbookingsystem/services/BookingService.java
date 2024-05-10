package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;


    public int getBookingCountForDate(LocalDate specificDate, String username) {
       return bookingRepository.getBookingCountForDate(specificDate, username);
    }

    public int getBookingCountForWeek(int year, int month, int weekNumber, String username) {
        return bookingRepository.getBookingCountForWeek(year, month, weekNumber, username);
    }

    public int getBookingCountForMonth(int year, int month, String username) {
        return bookingRepository.getBookingCountForMonth(year, month, username);
    }


    public List<Booking> getBookingsForMonth(int year, int month, String username) {
        return bookingRepository.getBookingsForMonth(year, month, username);
    }

    public List<Booking> getBookingsForDay(LocalDate date, String username) {
        return bookingRepository.getBookingsForDay(date, username);

    }

    //tror ikke denne skal være liste og skal justeres til visning af specifik booking
    public List<Booking> showBooking(int bookingId, String tattooUsername){
        return bookingRepository.showBooking(bookingId, tattooUsername);
    }

    public List<Booking> showBookingList(){
        return bookingRepository.showBookingList();
    }

    public double calculateTotalDurationOfBooking(Booking booking) {
        LocalTime startTime = booking.getStartTimeSlot();
        LocalTime endTime = booking.getEndTimeSlot();

        Duration duration = Duration.between(startTime, endTime);

        // total hours from duration -- so  smart toHours method
        long minutes = duration.toMinutes();

        // Add total hours to total booked hours
        double totalMinutes = minutes;
        System.out.println(totalMinutes);

        return totalMinutes;
    }

}
