package com.example.streetinkbookingsystem.service;

import com.example.streetinkbookingsystem.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public int getBookingCountForDate(LocalDate specificDate) {
       return bookingRepository.getBookingCountForDate(specificDate);
    }
}
