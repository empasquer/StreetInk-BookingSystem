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
    private JdbcTemplate jdbcTemplate;

    /**
     * Saves a project picture to the database.
     *
     * @param projectPicture the ProjectPicture object containing the booking ID and picture data to be saved
     * @Author Tara
     */
    public void saveProjectPictures(ProjectPicture projectPicture){
        String query = "INSERT INTO project_picture (booking_id, picture_data) VALUES (?, ?)";
        jdbcTemplate.update(query, projectPicture.getBookingId(), projectPicture.getPictureData());
    }

    /**
     * Retrieves a list of project pictures associated with a specific booking.
     *
     * @param bookingId the ID of the booking for which to retrieve project pictures
     * @return a list of ProjectPicture objects associated with the specified booking ID
     * @Author Tara
     */
    public List<ProjectPicture> getPicturesByBooking(int bookingId) {
        String query = "SELECT * FROM project_picture WHERE booking_id = ?";
        RowMapper<ProjectPicture> rowMapper = new BeanPropertyRowMapper<>(ProjectPicture.class);
        try {
            return jdbcTemplate.query(query, rowMapper, bookingId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    /**
     * @Summary Deletes a project picture from the database based on the given picture ID.
     * @param pictureId the ID of the picture to be deleted
     * @author Munazzah
     */
    public void deleteProjectPictureById(int pictureId) {
        String sql = "DELETE FROM project_picture WHERE id = ?";
        jdbcTemplate.update(sql, pictureId);
    }

    /**
     * @Summary Updates the project pictures for a given booking by deleting existing pictures
     * and inserting new ones.
     * @param bookingId the ID of the booking whose pictures are to be updated
     * @param pictureDataList a list of byte arrays representing the new pictures to be inserted
     *
     * @author Tara
     */
    public void updateProjectPictures(int bookingId, List<byte[]> pictureDataList) {
        // First, delete existing pictures for the booking
        String deleteQuery = "DELETE FROM project_picture WHERE booking_id = ?";
        jdbcTemplate.update(deleteQuery, bookingId);

        // Then, insert the new pictures
        String insertQuery = "INSERT INTO project_picture (booking_id, picture_data) VALUES (?, ?)";
        for (byte[] pictureData : pictureDataList) {
            jdbcTemplate.update(insertQuery, bookingId, pictureData);
        }
    }

}
