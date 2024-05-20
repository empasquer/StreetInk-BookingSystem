package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.ProjectPicture;
import com.example.streetinkbookingsystem.repositories.BookingRepository;
import com.example.streetinkbookingsystem.repositories.ProjectPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class ProjectPictureService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ProjectPictureRepository projectPictureRepository;

    @Transactional
    public void saveProjectPictures(int bookingId, ArrayList<byte[]> projectPictures){
        for (byte[] pictureData : projectPictures){
            ProjectPicture picture = new ProjectPicture();
            picture.setBookingId(bookingId);
            picture.setPictureData(pictureData);
            //bookingRepository.saveProjectPicture(bookingId, pictureData);
            projectPictureRepository.saveProjectPicture(picture);
        }
    }
}
