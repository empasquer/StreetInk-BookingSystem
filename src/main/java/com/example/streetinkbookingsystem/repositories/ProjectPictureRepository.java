package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.ProjectPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectPictureRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void saveProjectPicture(ProjectPicture projectPicture){
        String query = "INSERT INTO project_picture (booking_id, picture_data) VALUES (?, ?)";
        jdbcTemplate.update(query, projectPicture.getBookingId(), projectPicture.getPictureData());
    }

   /* public List<ProjectPicture> findBookingId(int bookingId){
        String query = "SELECT * FROM project_picture WHERE booking_id = ?";
        return jdbcTemplate.query(query, new Object[]{bookingId}, new ProjectPictureRowMapper());
    }

    */

}
