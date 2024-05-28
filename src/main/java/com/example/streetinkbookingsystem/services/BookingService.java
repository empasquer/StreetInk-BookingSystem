package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.ProjectPicture;
import com.example.streetinkbookingsystem.repositories.BookingRepository;
import com.example.streetinkbookingsystem.repositories.ProjectPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ProjectPictureRepository projectPictureRepository;

    /**
     * Creates a new booking with the specified details and saves associated project pictures.
     *
     * @param startTimeSlot the start time slot for the booking
     * @param endTimeSlot   the end time slot for the booking
     * @param date          the date of the booking
     * @param username      the username of the tattoo artist
     * @param projectTitle  the title of the project
     * @param projectDesc   the description of the project
     * @param personalNote  a personal note about the booking
     * @param isDepositPayed whether the deposit has been paid
     * @param pictureList   a list of picture data to be associated with the booking
     * @return the created Booking object
     * @author Tara
     */
    public Booking createNewBooking(LocalTime startTimeSlot,
                                    LocalTime endTimeSlot,
                                    LocalDate date,
                                    String username,
                                    String projectTitle,
                                    String projectDesc,
                                    String personalNote,
                                    boolean isDepositPayed,
                                    List<byte[]> pictureList){

        Booking savedBooking = bookingRepository.createNewBooking(startTimeSlot, endTimeSlot, date,
                username, projectTitle, projectDesc, personalNote, isDepositPayed);

        for(byte[] pictureData : pictureList){
            ProjectPicture picture = new ProjectPicture();
            picture.setPictureData(pictureData);
            picture.setBookingId(savedBooking.getId());
            projectPictureRepository.saveProjectPictures(picture);
        }
        return savedBooking;
    }

    /**
     * @summary Updates the booking with new parameters (or just the old ones).
     * First it saves the pictures to the booking, and afterward, it updates the actual booking
     *
     * @author Munazzah
     * @param bookingId
     * @param startTimeSlot
     * @param endTimeSlot
     * @param date
     * @param projectTitle
     * @param projectDesc
     * @param personalNote
     * @param isDepositPayed
     * @param pictureList
     */
    public void updateBooking(int bookingId, LocalTime startTimeSlot, LocalTime endTimeSlot,
                                 LocalDate date, String projectTitle, String projectDesc,
                                 String personalNote, boolean isDepositPayed,
                                 List<byte[]> pictureList) {

        for (byte[] pictureData : pictureList) {
            ProjectPicture picture = new ProjectPicture();
            picture.setPictureData(pictureData);
            picture.setBookingId(bookingId);
            projectPictureRepository.saveProjectPictures(picture);
        }

        bookingRepository.updateBooking(bookingId, startTimeSlot, endTimeSlot, date, projectTitle, projectDesc,
                personalNote, isDepositPayed);

    }


    public int getBookingCountForDate(LocalDate specificDate, String username) {
        return bookingRepository.getBookingCountForDate(specificDate, username);
    }

    public int getBookingCountForThisWeek(String username) {
        return bookingRepository.getBookingCountForThisWeek(username);
    }

    public int getBookingCountForMonth(int year, int month, String username) {
        return bookingRepository.getBookingCountForMonth(year, month, username);
    }

    public List<Booking> getBookingsForMonth(int year, int month, String username) {
        return bookingRepository.getBookingsForMonth(year, month, username);
    }

    public List<Booking> getBookingsForDay(LocalDate date, String username) {
        return bookingRepository.getBookingsForDay(date, username);
    }

    public Booking getBookingDetail(int bookingId) {
        return bookingRepository.getBookingDetails(bookingId);
    }


    public double calculateTotalDurationOfBooking(Booking booking) {
        LocalTime startTime = booking.getStartTimeSlot();
        LocalTime endTime = booking.getEndTimeSlot();

        Duration duration = Duration.between(startTime, endTime);

        // total hours from duration -- so  smart toHours method
        long minutes = duration.toMinutes();

        // Add total hours to total booked hours
        double totalMinutes = minutes;


        return totalMinutes;
    }

    public List<Booking> getBookingsByClientId(int clientId) {
        return bookingRepository.getBookingsByClientId(clientId);
    }

    /**
     * @author Munazzah
     * @param bookingId That needs to be deleted
     */
    public void deleteBooking(int bookingId) {
        bookingRepository.deleteBooking(bookingId);
    }
}