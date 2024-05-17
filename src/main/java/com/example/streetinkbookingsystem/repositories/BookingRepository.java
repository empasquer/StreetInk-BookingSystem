package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // Gets all bookings for date, but only information needed to display the block
    public List<Booking> getBookingsForDay( LocalDate date, String username){
        String query = "SELECT * FROM booking JOIN client ON booking.client_id =  client.id " +
                "LEFT JOIN project_picture On booking.id = project_picture.booking_id WHERE booking.username = ? AND date =?;";
        RowMapper<Booking> rowMapper = (rs, rowNum) -> {
            Booking booking = new Booking();
            booking.setId(rs.getInt("booking.id"));
            booking.setStartTimeSlot(rs.getTime("start_time_slot").toLocalTime());
            booking.setEndTimeSlot(rs.getTime("end_time_slot").toLocalTime());
            booking.setProjectTitle(rs.getString("project_title"));

            Client client = new Client();

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
        return  jdbcTemplate.queryForObject(query,rowMapper, bookingId);
    }

    /**
     * @author Tara
     * @return list af bookinger
     */
    public List<Booking> showBookingList(){
        String query = "SELECT * FROM booking";
        RowMapper rowMapper = new BeanPropertyRowMapper(Booking.class);
        return jdbcTemplate.query(query, rowMapper);
    }

    public void createNewBooking (LocalTime startTimeSlot, LocalTime endTimeSlot, LocalDate date,
                                  String username, String projectTitle, String projectDesc, String personalNote,
                                  boolean isDepositPayed){
        String query = "INSERT INTO booking (start_time_slot, end_time_slot, date, client_id, username, " +
                "project_title, project_desc, personal_note, is_deposit_payed)" +
                "values(?, ?, ?, 1, ?, ?, ?, ?, ?);";
        // givet client_id 1, så den pr. default har "Unknown Client",
        // indtil client view, hvor vi opdaterer til ny eller eksisterende client
        jdbcTemplate.update(query, startTimeSlot, endTimeSlot, date, username, projectTitle, projectDesc,
                personalNote, isDepositPayed);
    }

    //Retunerer antallet af booking for en bestemt dato og username
    public int getBookingCountForDate(LocalDate specificDate, String username) {
        // Denne SQL-forespørgsel tæller antallet af rækker i tabellen booking,
        // der matcher den givne dato (specificDate) og brugernavn (username).
        String query = "SELECT COUNT(*) AS booking_count FROM booking WHERE date = ? AND username = ?";
        //queryForObject er en metode, der udfører en forespørgsel og returnerer et enkelt resultat.
        //Integer.class, angiver at resultatet skal mappes til en Integer.
        return jdbcTemplate.queryForObject(query, Integer.class, specificDate, username);
        //Selvom COUNT(*) altid retunerer en int, så skal man stadig bruge Integer.class, da queryForObject
        // kan retunere forskellige værdier, og har brug for specificering
    }

    public int getBookingCountForThisWeek(String username) {
        // Calculate the start date of the week based on the provided year, month, and week number

        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Calculate the end date of the week by adding 6 days to the start date (assuming a week ends on Sunday)
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Query the database to get the booking count for the specified week
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

    public List<Booking> getBookingsByClientId(int clientId) {
        String query = "SELECT * FROM booking WHERE client_id = ? ORDER BY date DESC";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);
        return jdbcTemplate.query(query, rowMapper, clientId);
    }

  
    public List<Booking> getBookingsForDay(LocalDate date, String username) {
        String query = "SELECT * FROM booking WHERE date = ? AND username = ?";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);
        return jdbcTemplate.query(query, rowMapper, date, username);
    }



}
