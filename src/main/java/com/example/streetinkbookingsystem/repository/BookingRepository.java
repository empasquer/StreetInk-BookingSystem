package com.example.streetinkbookingsystem.repository;

import com.example.streetinkbookingsystem.models.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public int getBookingCountForDate(LocalDate specificDate) {
       String query = "SELECT COUNT(*) AS booking_count  FROM booking WHERE date = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, specificDate);

    }
}
