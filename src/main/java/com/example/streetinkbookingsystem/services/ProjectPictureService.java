package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.ProjectPicture;
import com.example.streetinkbookingsystem.repositories.BookingRepository;
import com.example.streetinkbookingsystem.repositories.ProjectPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectPictureService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ProjectPictureRepository projectPictureRepository;


    /**
     * Converts the data of the pictures into base64-encoded strings.
     *
     * @param projectPictures the list of ProjectPicture objects to be converted
     * @return a list of base64-encoded strings representing the picture data
     * @Summary is used to convert the data of the pictures into base64-strings
     * @Author Tara
     */
    public List<String> convertToBase64(List<ProjectPicture> projectPictures){
        List<String> base64Images = new ArrayList<>();
        for (ProjectPicture picture : projectPictures){
            base64Images.add(Base64.getEncoder().encodeToString(picture.getPictureData()));
        }
        return base64Images;
    }

    /**
     * @author Munazzah
     * @param pictureList A list of picture IDs representing the project pictures to be deleted
     */
    public void deleteProjectPictures(List<Integer> pictureList) {
        for (Integer pictureId : pictureList){
            projectPictureRepository.deleteProjectPictureById(pictureId);
        }
    }

    /**
     * @author Munazzah
     * @param bookingId Input
     * @return List of String that has pictures in it
     */
    public List<String> getPicturesByBooking(int bookingId) {
        return convertToBase64(projectPictureRepository.getPicturesByBooking(bookingId));
    }

    /**
     * @author Munazzah
     * @param bookingId Input
     * @return List of project pictures
     */
    public List<ProjectPicture> getPicturesAsObjects(int bookingId) {
        return projectPictureRepository.getPicturesByBooking(bookingId);
    }

    /**
     * Updates the project pictures associated with a specific booking.
     *
     * @param bookingId       the ID of the booking whose project pictures are to be updated
     * @param pictureDataList the list of byte arrays containing the updated picture data
     * @Author Tara
     */
    public void updateProjectPictures(int bookingId, List<byte[]> pictureDataList) {
        projectPictureRepository.updateProjectPictures(bookingId,pictureDataList);
    }

}
