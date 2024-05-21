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

    @Transactional
    public void saveProjectPictures(int bookingId, List<byte[]> pictureList){

       for (byte[] pictureData : pictureList){
           ProjectPicture picture = new ProjectPicture();
           picture.setBookingId(bookingId);
           picture.setPictureData(pictureData);
           projectPictureRepository.saveProjectPicture(picture);
       }

       /* for (MultipartFile file : projectPictures){
            if (!file.isEmpty()){
                try {
                    ProjectPicture picture = new ProjectPicture();
                    picture.setBookingId(bookingId);
                    picture.setPictureData(file.getBytes());
                    projectPictureRepository.saveProjectPicture(picture);
                } catch (IOException e){ // Læs om hvorfor lige præcis denne exception
                    e.printStackTrace();
                }
            }

        }

        */
    }

    public List<String> convertToBase64(List<ProjectPicture> projectPictures){
        List<String> base64Images = new ArrayList<>();
        for (ProjectPicture picture : projectPictures){
            base64Images.add(Base64.getEncoder().encodeToString(picture.getPictureData()));
        }
        return base64Images;
    }

    //Bruges til at konventere billeddataene til Base64-streng

    public String getBase64String(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }

}
