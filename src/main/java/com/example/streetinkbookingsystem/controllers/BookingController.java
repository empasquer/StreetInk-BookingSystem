package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.ProjectPicture;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    LoginService loginService;
    @Autowired
    TattooArtistService tattooArtistService;
    @Autowired
    ProjectPictureService projectPictureService;
    @Autowired
    ClientService clientService;
    @Autowired
    EmailService emailService;

    /**
     * @Author Nanna
     * @param model
     * @param session
     * @param bookingId
     * @param username
     * @return
     */
    @GetMapping("/booking")
     public String booking(Model model, HttpSession session, @RequestParam int bookingId, @RequestParam String username){
        /*if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

         */
        //kun lavet så jeg kunne rette design uden at logge ind hele tiden.
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        tattooArtist.setUsername("Hanzoo");

        Booking booking = bookingService.getBookingDetail(bookingId);
        model.addAttribute("booking", booking);

        // Henter billeder fra den specifikke booking
        List<String> base64Images = projectPictureService.convertToBase64(booking.getProjectPictures());
        model.addAttribute("base64Images", base64Images);

        return "home/booking";
    }


    /**
     * @Author Tara
     * @param model
     * @param session Used to determine if the user is logged in or not. User will be redirected
     *                to index page if not logged in.
     * @param date Is used, sp that we create af booking on the specific date.
     * @return den gemte booking.
     */
    @GetMapping("/create-new-booking")
    public String createNewBooking(Model model, HttpSession session, @RequestParam LocalDate date, @RequestParam (required = false) Integer bookingId ){
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);
        model.addAttribute("bookingId", bookingId);
        if (bookingId != null) {
            Booking booking =bookingService.getBookingDetail(bookingId);
            model.addAttribute("booking",booking);
        }




        model.addAttribute("date", date);

        return "home/create-new-booking";
    }

    /**
     * @Author Tara
     * @param startTimeSlot
     * @param endTimeSlot
     * @param date
     * @param session
     * @param projectTitle
     * @param projectDesc
     * @param personalNote
     * @param isDepositPayed
     * @param projectPictures
     * @param action
     * @param redirectAttributes
     * @return add-client eller existing-client
     */
    @PostMapping("/save-tattoo-info")
    public String saveNewBooking(@RequestParam LocalTime startTimeSlot,
                                 @RequestParam LocalTime endTimeSlot,
                                 @RequestParam LocalDate date,
                                 HttpSession session,
                                 @RequestParam String projectTitle,
                                 @RequestParam String projectDesc,
                                 @RequestParam String personalNote,
                                 @RequestParam(name = "isDepositPayed", defaultValue = "false") boolean isDepositPayed,
                                 //Multipartfile er en datatypen. Disse objekter repræsenterer filer, som er blevet uploadet via en html-formular. Der kan være flere filer som er uploadet.
                                 @RequestParam("projectPictures") MultipartFile[] projectPictures,
                                 @RequestParam String action, // Button action parameter
                                 @RequestParam(required = false) Integer bookingId,
                                 RedirectAttributes redirectAttributes, Model model) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your session has expired. Please log in again.");
            return "redirect:/";
        }


            List<byte[]> pictureList = Stream.of(projectPictures).filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            return file.getBytes();
                        } catch (IOException e){
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .collect(Collectors.toList());

        if (endTimeSlot.isBefore(startTimeSlot)) {
            model.addAttribute("errorMessage", "End time cannot be before start time.");
            model.addAttribute("date", date);
            return "home/create-new-booking";
        }


        Booking booking;
        if (bookingId != null) {
            // Update existing booking
            bookingService.updateBooking(bookingId, startTimeSlot, endTimeSlot, date, projectTitle, projectDesc, personalNote, isDepositPayed, pictureList);
            booking = bookingService.getBookingDetail(bookingId);
            projectPictureService.updateProjectPictures(bookingId, pictureList);
        } else {
            // Create new booking
            booking = bookingService.createNewBooking(startTimeSlot, endTimeSlot, date, username, projectTitle, projectDesc, personalNote, isDepositPayed, pictureList);
           // projectPictureService.saveProjectPictures(booking.getId(), pictureList);
        }
        int savedBookingId = booking.getId();
        if ("new-client".equals(action)) {
            return "redirect:/add-client?bookingId=" + savedBookingId + "&username=" + username + "&date=" + date;
        } else if ("existing-client".equals(action)) {
            return "redirect:/choose-client?bookingId=" + savedBookingId + "&username=" + username + "&date=" + date;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid action.");
            return "redirect:/create-new-booking?date=" + date;
        }
    }

    /**
     * @Author Tara
     * @param model
     * @param session
     * @param bookingId
     * @param username
     * @param clientId
     * @return booking-preview
     */
    @GetMapping("/booking-preview")
    public String bookingPreview (Model model, HttpSession session, @RequestParam int bookingId,
                                  @RequestParam String username,
                                  @RequestParam int clientId){

        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (!loggedIn) {
            return "redirect:/";
        }

        //tilføjer den "nye" ClientId til bookingen
        clientService.updateClientOnBooking(bookingId, clientId);

        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("username", session.getAttribute(username));
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        Booking booking = bookingService.getBookingDetail(bookingId);
        model.addAttribute("booking", booking);

        // Henter billeder fra den specifikke booking
        List<String> base64Images = projectPictureService.convertToBase64(booking.getProjectPictures());
        model.addAttribute("base64Images", base64Images);

        return "home/booking-preview";
    }

    @GetMapping("/cancel-booking")
    public String cancelBooking(@RequestParam int bookingId, @RequestParam String date, Model model) {
        bookingService.deleteBooking(bookingId);
        System.out.println("here");
        return "redirect:/day?date=" + date;
    }
    @GetMapping("/save-booking")
    public String saveBooking(@RequestParam int bookingId, HttpSession session, Model model) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);
        String username = (String) session.getAttribute("username");
        Booking booking =  bookingService.getBookingDetail(bookingId);
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        Client client =booking.getClient();
        String bookingEnd =booking.getEndTimeSlot().format(DateTimeFormatter.ofPattern("HH:mm"));
        String bookingStart =booking.getStartTimeSlot().format(DateTimeFormatter.ofPattern("HH:mm"));
        String bookingDate = booking.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //Send to mail template
        Context context = new Context();
        context.setVariable("ClientFirstName", client.getFirstName());
        context.setVariable("ArtistFirstName", tattooArtist.getFirstName());
        context.setVariable("ArtistLastName", tattooArtist.getLastName());
        context.setVariable("ArtistPhone", tattooArtist.getPhoneNumber());
        context.setVariable("ArtistEmail", tattooArtist.getEmail());
        context.setVariable("ArtistFacebook", tattooArtist.getFacebook());
        context.setVariable("ArtistInstagram", tattooArtist.getInstagram());
        context.setVariable("BookingStart", bookingStart);
        context.setVariable("BookingEnd", bookingEnd);
        context.setVariable("BookingDate", bookingDate);
        context.setVariable("BookingTitle", booking.getProjectTitle());
        context.setVariable("BookingDescription", booking.getProjectDesc());
        emailService.sendConfirmationMail(client.getEmail(), context);
        return "redirect:/booking?bookingId="+bookingId + "&username=" + tattooArtist.getUsername();
    }

    @GetMapping("/edit-booking")
    public String editBooking(Model model, HttpSession session, @RequestParam("bookingId") int bookingId, RedirectAttributes redirectAttributes) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        Booking booking = bookingService.getBookingDetail(bookingId);
        model.addAttribute("booking", booking);

        //Henter de billeder der allerede findes
        List<ProjectPicture> projectPictures = projectPictureService.getPicturesAsObjects(bookingId);
        model.addAttribute("projectPictures", projectPictures);
        List<String> base64Images = projectPictureService.getPicturesByBooking(bookingId);
        model.addAttribute("base64Images", base64Images);

        return "home/edit-booking";
    }

    @PostMapping("/edit-booking")
    public String updateBooking(@RequestParam LocalTime startTimeSlot, @RequestParam LocalTime endTimeSlot, @RequestParam LocalDate date,
                                @RequestParam String projectTitle, @RequestParam String projectDesc,
                                @RequestParam String personalNote, @RequestParam boolean isDepositPayed, @RequestParam(required = false) List<Integer> deletePictures,
                                @RequestParam("projectPictures") MultipartFile[] projectPictures,
                                @RequestParam int bookingId, Model model, HttpSession session) {


        if (deletePictures != null) {
            projectPictureService.deleteProjectPictures(deletePictures);
        }

        List<byte[]> pictureList = Stream.of(projectPictures).filter(file -> !file.isEmpty())
                .map(file -> {
                    try {
                        return file.getBytes();
                    } catch (IOException e){
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());

        bookingService.updateBooking(bookingId, startTimeSlot, endTimeSlot, date, projectTitle, projectDesc, personalNote, isDepositPayed, pictureList);
        return "redirect:/booking?bookingId=" + bookingId + "&username=" + session.getAttribute("username");
    }

    @GetMapping("/confirm-delete-booking")
    public String confirmDeleteBooking(Model model, HttpSession session, @RequestParam int bookingIdToDelete) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        Booking booking = bookingService.getBookingDetail(bookingIdToDelete);
        model.addAttribute("booking", booking);
        model.addAttribute("bookingIdToDelete", bookingIdToDelete);

        return "home/confirm-delete-booking";
    }

    @PostMapping("/confirm-delete-booking")
    public String deleteBookingWithWarning(@RequestParam int bookingIdToDelete, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        redirectAttributes.addAttribute("bookingIdToDelete", bookingIdToDelete);
        redirectAttributes.addAttribute("showConfirmation", true);
        return "redirect:/delete-booking";
    }

    /**
     *
     * @param bookingIdToDelete
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/delete-booking")
    public String deleteBooking(@RequestParam int bookingIdToDelete, HttpSession session, Model model) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }

        loginService.addLoggedInUserInfo(model, session, tattooArtistService);
        bookingService.deleteBooking(bookingIdToDelete);
        return "redirect:/calendar";
    }




}



