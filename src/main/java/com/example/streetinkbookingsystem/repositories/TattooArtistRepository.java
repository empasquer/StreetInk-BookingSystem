package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class TattooArtistRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Retrieves a list of all tattoo artists from the database.
     *
     * @return a list of all TattooArtist objects
     * @author Tara
     */
    public List<TattooArtist> showTattooArtists(){
        String query = "SELECT * FROM tattoo_artist;";
        RowMapper rowMapper = new BeanPropertyRowMapper(TattooArtist.class);
        return jdbcTemplate.query(query, rowMapper);
    }

    /**
     * @author Munazzah
     * @param profileUsername The username of the tattoo artist to be retrieved
     * @return A TattooArtist object if found, or null if no artist with the specified username exists
     */
    public TattooArtist getTattooArtistByUsername(String profileUsername) {
        String query = "SELECT * FROM tattoo_artist WHERE username = ?";
        RowMapper<TattooArtist> rowMapper = new BeanPropertyRowMapper<>(TattooArtist.class);
        try {
            return jdbcTemplate.queryForObject(query, rowMapper, profileUsername);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * @author Munazzah
     * @param username The username of the tattoo artist whose password needs to be updated
     * @param password The new password to be set for the tattoo artist
     */
    public void updatePassword(String username, String password) {
        String query = "UPDATE tattoo_artist SET password=? WHERE username=?";
        jdbcTemplate.update(query, password, username);
    }

    /**
     * @author Munazzah
     * @param username The username of the tattoo artist whose password needs to be retrieved
     * @return The password of the tattoo artist, or null if no artist is found with the given username
     */
    public String getPassword(String username) {
        String query = "SELECT password FROM tattoo_artist WHERE username=?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * @author Munazzah
     * @param username The username of the tattoo artist whose email needs to be retrieved
     * @return The email of the tattoo artist, or null if no artist is found with the given username
     */
    public String getEmail(String username) {
        String query = "SELECT email FROM tattoo_artist WHERE username=?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * @author Nanna
     * @param username the username of the profile that should change status
     * @param status the status that the admin boolean should change to
     */
    public void changeAdminStatus(String username, boolean status) {
        String query = "UPDATE tattoo_artist SET is_admin=? WHERE username=?";
        jdbcTemplate.update(query, status, username);
    }

    /**
     * @author Nanna
     * @param profileToDelete the username of the profile that will be deleted
     */
    public void deleteProfileByUsername(String profileToDelete) {
        String query = "DELETE FROM tattoo_artist WHERE username =?";
         jdbcTemplate.update(query,profileToDelete);
    }


    /**
     * @author Nanna
     * @param username      The username of the tattoo artist.
     * @param firstname     The first name of the tattoo artist.
     * @param lastName      The last name of the tattoo artist.
     * @param password      The password for the profile
     * @param facebookUrl   The Facebook URL of the tattoo artist.
     * @param instagramUrl  The Instagram URL of the tattoo artist.
     * @param phone         The phone number of the tattoo artist.
     * @param email         The email address of the tattoo artist.
     * @param avgWorkHours  The average work hours per day of the tattoo artist.
     * @param isAdmin       Indicatates if the tattoo artist is an admin.
     * @param pictureData   Optional profile picture of the tattoo artist.
     */
    public void createProfile(String username, String firstname, String lastName, String password, String facebookUrl,  String instagramUrl, int phone, String email, int avgWorkHours, boolean isAdmin, Optional<byte[]> pictureData) {
        String query = "INSERT INTO tattoo_artist(username, first_name, last_name, password, email, phone_number, facebook, instagram, avg_work_hours, is_admin, profile_picture) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        // Unwrap the Optional and set to null if not present
        byte[] picture = pictureData.orElse(null);
            jdbcTemplate.update(query, username, firstname, lastName, password, email, phone, facebookUrl, instagramUrl, avgWorkHours, isAdmin,picture);
        }

    /**
     * @author Nanna
     * @param firstName       The updated first name of the tattoo artist.
     * @param lastName        The updated last name of the tattoo artist.
     * @param email           The updated email address of the tattoo artist.
     * @param phoneNumber     The updated phone number of the tattoo artist.
     * @param facebook        The updated Facebook URL of the tattoo artist.
     * @param instagram       The updated Instagram URL of the tattoo artist.
     * @param avgWorkHours    The updated average work hours per day of the tattoo artist.
     * @param newUsername     The new username for the tattoo artist.
     * @param currentUsername The current username of the tattoo artist.
     * @param pictureData     Optional updated profile picture the tattoo artist.
     */
    public void updateTattooArtist(String firstName, String lastName, String email, int phoneNumber, String facebook, String instagram, int avgWorkHours, String newUsername, String currentUsername, Optional<byte[]> pictureData) {
        StringBuilder query = new StringBuilder("UPDATE tattoo_artist SET first_name = ?, email = ?, phone_number = ?, avg_work_hours = ?, username = ?");
        List<Object> params = new ArrayList<>(Arrays.asList(firstName, email, phoneNumber, avgWorkHours, newUsername));

        if (lastName != null) {
            query.append(", last_name = ?");
            params.add(lastName);
        }

        if (facebook != null) {
            query.append(", facebook = ?");
            params.add(facebook);
        }

        if (instagram != null) {
            query.append(", instagram = ?");
            params.add(instagram);
        }

        if (pictureData.isPresent()) {
            query.append(", profile_picture = ?");
            params.add(pictureData.get());
        }

        query.append(" WHERE username = ?");
        params.add(currentUsername);

        try {
            jdbcTemplate.update(query.toString(), params.toArray());
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Something went wrong");
        }
    }

}
