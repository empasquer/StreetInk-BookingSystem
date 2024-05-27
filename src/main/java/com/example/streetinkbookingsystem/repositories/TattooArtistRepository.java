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
    JdbcTemplate jdbcTemplate;


    /**
     * @author Tara
     * @return liste af alle tatovørerne
     */
    public List<TattooArtist> showTattooArtists(){
        String query = "SELECT * FROM tattoo_artist;";
        RowMapper rowMapper = new BeanPropertyRowMapper(TattooArtist.class);
        return jdbcTemplate.query(query, rowMapper);
    }

    /**
     * @author Munazzah
     * @param profileUsername
     * @return TattooArtist
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
     * @param username
     * @param password
     */
    public void updatePassword(String username, String password) {
        String query = "UPDATE tattoo_artist SET password=? WHERE username=?";
        jdbcTemplate.update(query, password, username);
    }

    /**
     * @author Munazzah
     * @param username
     * @return String
     */
    public String getPassword(String username) {
        String query = "SELECT password FROM tattoo_artist WHERE username=?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getEmail(String username) {
        String query = "SELECT email FROM tattoo_artist WHERE username=?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void changeAdminStatus(String username, boolean status) {
        String query = "UPDATE tattoo_artist SET is_admin=? WHERE username=?";
        jdbcTemplate.update(query, status, username);
    }

    public void deleteProfileByUsername(String profileToDelete) {
        String query = "DELETE FROM tattoo_artist WHERE username =?";
         jdbcTemplate.update(query,profileToDelete);
    }



    public void createProfile(String username, String firstname, String lastName, String password, String facebookUrl,  String instagramUrl, int phone, String email, int avgWorkHours, boolean isAdmin, Optional<byte[]> pictureData) {
        String query = "INSERT INTO tattoo_artist(username, first_name, last_name, password, email, phone_number, facebook, instagram, avg_work_hours, is_admin, profile_picture) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        // Unwrap the Optional and set to null if not present
        byte[] picture = pictureData.orElse(null);
            jdbcTemplate.update(query, username, firstname, lastName, password, email, phone, facebookUrl, instagramUrl, avgWorkHours, isAdmin,picture);
        }

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
