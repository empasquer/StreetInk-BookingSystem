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

    /**
     * @author Tara
     * @param startTimeSlot
     * @param endTimeSlot
     * @param date
     * @param username
     * @param projectTitle
     * @param projectDesc
     * @param personalNote
     * @param isDepositPayed
     */
    public void createNewBooking(LocalTime startTimeSlot, LocalTime endTimeSlot, LocalDate date,
                                 String username, String projectTitle, String projectDesc, String personalNote,
                                 boolean isDepositPayed){
        bookingRepository.createNewBooking(startTimeSlot, endTimeSlot, date, username, projectTitle, projectDesc,
                personalNote, isDepositPayed);
    }


    public int getBookingCountForDate(LocalDate specificDate, String username) {
        return bookingRepository.getBookingCountForDate(specificDate, username);
    }

    public int getBookingCountForThisWeek(String username) {
        return bookingRepository.getBookingCountForThisWeek(username);
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
    public Booking getBookingDetail(int bookingId) {
        return bookingRepository.getBookingDetails(bookingId);

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


        return totalMinutes;
    }

}