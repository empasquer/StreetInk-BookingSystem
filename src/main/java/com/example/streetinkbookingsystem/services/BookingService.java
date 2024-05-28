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
     * @author Tara
     * @param startTimeSlot
     * @param endTimeSlot
     * @param date
     * @param username
     * @param projectTitle
     * @param projectDesc
     * @param personalNote
     * @param isDepositPayed
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

    /**
     * @author Nanna
     * @param specificDate the date to search for bookings on
     * @param username the tattooartist that has the bookings
     * @return
     */
    public int getBookingCountForDate(LocalDate specificDate, String username) {
        return bookingRepository.getBookingCountForDate(specificDate, username);
    }

    /**
     * @author Emma
     * @param username of the tattooArtist of whom we want the number of booking
     * @return the number of booking for a week for at tattooArtist
     */
    public int getBookingCountForThisWeek(String username) {
        return bookingRepository.getBookingCountForThisWeek(username);
    }

    /**
     * @author Emma
     * @param year the year of the month we want the bookings off
     * @param month the month we want the bookings off
     * @param username the tattooartist of the bookings
     * @return the number of bookings in a month for a tattooartist
     */
    public int getBookingCountForMonth(int year, int month, String username) {
        return bookingRepository.getBookingCountForMonth(year, month, username);
    }

    /**
     * @author Nanna
     * @param year the year of the month we wan thte bookings of
     * @param month the month that we want
     * @param username the tattooArtist connected to the bookings
     * @return a list of bookings related to the tattooArtist in the given month
     */
    public List<Booking> getBookingsForMonth(int year, int month, String username) {
        return bookingRepository.getBookingsForMonth(year, month, username);
    }

    /**
     * @author Nanna
     * @param date the date we wish to view
     * @param username the tattooArtist connected to the bookings
     * @return a list of bookings on a date with a tattooArtist
     */
    public List<Booking> getBookingsForDay(LocalDate date, String username) {
        return bookingRepository.getBookingsForDay(date, username);
    }

    /**
     * @author Nanna
     * @param bookingId the id of the booking we want to fetch
     * @return a booking
     */
    public Booking getBookingDetail(int bookingId) {
        return bookingRepository.getBookingDetails(bookingId);
    }

    /**
     * @auhtor Emma
     * @param booking The booking we want to calculate the duration of.
     * @return a double representing the total duration of the booking in minutes.
     */
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