package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BookingRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Booking> showBooking(int bookingId, String tattooUsername){
        String query = "SELECT c.first_name, c.last_name, c.phone_number, c.email, " +
                "b.date, b.start_time_slot, b.end_time_slot, b.is_deposit_payed, " +
                "b.project_title, b.project_desc, b.personal_note" +
        " FROM client c" +
        " JOIN booking b ON b.client_id = c.id " +
        " WHERE b.id = ? AND b.username = ?;";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);
        return jdbcTemplate.query(query, rowMapper, bookingId, tattooUsername);
    }

    public List<Booking> showBookingList(){
        String query = "SELECT * FROM booking";
        RowMapper rowMapper = new BeanPropertyRowMapper(Booking.class);
        return jdbcTemplate.query(query, rowMapper);
    }


    public int getBookingCountForDate(LocalDate specificDate) {
       String query = "SELECT COUNT(*) AS booking_count  FROM booking WHERE date = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, specificDate);

    }
}
