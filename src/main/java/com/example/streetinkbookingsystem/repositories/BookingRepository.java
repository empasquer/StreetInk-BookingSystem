package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.TattooArtist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
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

    // Gets all bookings for date, but only information needed to display the block
    public List<Booking> getBookingsForDay( LocalDate date, String username){
        String query = "SELECT * FROM booking JOIN client ON booking.client_id =  client.id " +
                "LEFT JOIN project_picture On booking.id = project_picture.booking_id WHERE booking.username = ? AND date =?;";
        RowMapper<Booking> rowMapper = (rs, rowNum) -> {
            Booking booking = new Booking();
            System.out.println(booking);
            booking.setId(rs.getInt("booking.id"));
            booking.setStartTimeSlot(rs.getTime("start_time_slot").toLocalTime());
            booking.setEndTimeSlot(rs.getTime("end_time_slot").toLocalTime());
            booking.setProjectTitle(rs.getString("project_title"));

            Client client = new Client();
            System.out.println(client);
            client.setId(rs.getInt("client.id"));
            client.setFirstName(rs.getString("first_name"));
            client.setLastName(rs.getString("last_name"));
            booking.setClient(client);
            return booking;
        };
        return jdbcTemplate.query(query,rowMapper,username,date);
    }

    //Gets a specific booking with all information
    public Booking getBookingDetails(int bookingId){
        String query = "SELECT * FROM booking JOIN client ON booking.client_id =  client.id " +
                "LEFT JOIN project_picture On booking.id = project_picture.booking_id WHERE booking.id = ?;";
        RowMapper<Booking> rowMapper = (rs, rowNum) -> {
            Booking booking = new Booking();
            booking.setId(rs.getInt("booking.id"));
            booking.setStartTimeSlot(rs.getTime("start_time_slot").toLocalTime());
            booking.setEndTimeSlot(rs.getTime("end_time_slot").toLocalTime());
            booking.setDate(rs.getDate("date").toLocalDate());
            booking.setProjectTitle(rs.getString("project_title"));
            booking.setProjectDesc(rs.getString("project_desc"));
            booking.setPersonalNote(rs.getString("personal_note"));
            booking.setDepositPayed(rs.getBoolean("is_deposit_payed"));
            Byte picture = rs.getByte("picture_data");
            if (picture != null) {
                ArrayList<Byte[]> projectPictures = new ArrayList<>(picture);
                booking.setProjectPictures(projectPictures);
            }

            Client client = new Client();
            client.setId(rs.getInt("client.id"));
            client.setFirstName(rs.getString("first_name"));
            client.setLastName(rs.getString("last_name"));
            client.setEmail(rs.getString("email"));
            client.setPhoneNumber(rs.getInt("phone_number"));
            client.setDescription(rs.getString("description"));

            booking.setClient(client);
            return booking;
        };
        return (Booking) jdbcTemplate.query(query,rowMapper);
    }
    public List<Booking> showBookingList(){
        String query = "SELECT * FROM booking";
        RowMapper rowMapper = new BeanPropertyRowMapper(Booking.class);
        return jdbcTemplate.query(query, rowMapper);
    }


    public int getBookingCountForDate(LocalDate specificDate, String username) {
        String query = "SELECT COUNT(*) AS booking_count FROM booking WHERE date = ? AND username = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, specificDate, username);
    }

    public int getBookingCountForWeek(int year, int month, int weekNumber, String username) {
        LocalDate startOfWeek = LocalDate.of(year, month, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))
                .plusWeeks(weekNumber - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6); // Assuming a week ends on Sunday

        String query = "SELECT COUNT(*) AS booking_count FROM booking WHERE date BETWEEN ? AND ? AND username = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, startOfWeek, endOfWeek, username);
    }


    public int getBookingCountForMonth(int year, int month, String username) {
        // Finde ud af start og slut dag på måneden
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        String query = "SELECT COUNT(*) AS booking_count FROM booking WHERE date BETWEEN ? AND ? AND username = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, startOfMonth, endOfMonth, username);
    }

    public List<Booking> getBookingsForMonth(int year, int month, String username) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        String query = "SELECT * FROM booking WHERE date BETWEEN ? AND ? AND username = ?";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);
        return jdbcTemplate.query(query, rowMapper, startDate, endDate, username);
    }

    /**
    public List<Booking> getBookingsForDay(LocalDate date, String username) {
        String query = "SELECT * FROM booking WHERE date = ? AND username = ?";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);
        return jdbcTemplate.query(query, rowMapper, date, username);
    }
     **/
}
