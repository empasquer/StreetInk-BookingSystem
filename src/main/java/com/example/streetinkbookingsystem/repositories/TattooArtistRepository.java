package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TattooArtistRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public List<TattooArtist> showTattooArtists(){
        String query = "SELECT * FROM tattoo_artist;";
        RowMapper rowMapper = new BeanPropertyRowMapper(TattooArtist.class);
        return jdbcTemplate.query(query, rowMapper);
    }

    public TattooArtist getTattooArtistByUsername(String profileUsername) {
        String query = "SELECT * FROM tattoo_artist WHERE username = ?";
        RowMapper<TattooArtist> rowMapper = new BeanPropertyRowMapper<>(TattooArtist.class);
        try {
            return jdbcTemplate.queryForObject(query, rowMapper, profileUsername);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updatePassword(String username, String password) {
        String query = "UPDATE tattoo_artist SET password=? WHERE username=?";
        jdbcTemplate.update(query, password, username);
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
}
