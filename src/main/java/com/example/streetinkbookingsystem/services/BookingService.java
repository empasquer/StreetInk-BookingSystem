package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.repositories.BookingRepository;
import com.example.streetinkbookingsystem.repositories.TattooArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    //tror ikke denne skal være liste og skal justeres til visning af specifik booking
    public List<Booking> showBooking(int bookingId, String tattooUsername){
        return bookingRepository.showBooking(bookingId, tattooUsername);
    }

    public List<Booking> showBookingList(){
        return bookingRepository.showBookingList();
    }

    public int getBookingCountForDate(LocalDate specificDate) {
       return bookingRepository.getBookingCountForDate(specificDate);
    }

}
