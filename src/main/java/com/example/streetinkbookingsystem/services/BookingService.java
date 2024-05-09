package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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


}
