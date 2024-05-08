package com.example.streetinkbookingsystem.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class BookingRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int getBookingCountForDate(LocalDate specificDate) {
       String query = "SELECT COUNT(*) AS booking_count  FROM booking WHERE date = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, specificDate);

    }
}
