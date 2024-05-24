package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.ProjectPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectPictureRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void saveProjectPicture(ProjectPicture projectPicture){
        String query = "INSERT INTO project_picture (booking_id, picture_data) VALUES (?, ?)";
        jdbcTemplate.update(query, projectPicture.getBookingId(), projectPicture.getPictureData());
    }

    public List<ProjectPicture> getPicturesByBooking(int bookingId) {
        String query = "SELECT * FROM project_picture WHERE booking_id = ?";
        RowMapper<ProjectPicture> rowMapper = new BeanPropertyRowMapper<>(ProjectPicture.class);
        try {
            return jdbcTemplate.query(query, rowMapper, bookingId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public void deleteProjectPictureById(int pictureId) {
        String sql = "DELETE FROM project_picture WHERE id = ?";
        jdbcTemplate.update(sql, pictureId);
    }


}
