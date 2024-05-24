package com.example.streetinkbookingsystem.controllers;
import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.repositories.TattooArtistRepository;
import com.example.streetinkbookingsystem.services.BookingService;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import com.example.streetinkbookingsystem.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    BookingService bookingService;
    @Autowired
    LoginService loginService;
    @Autowired
    TattooArtistService tattooArtistService;

    /**
     * @author Nanna
     * @param bookingId used to find the booking, its client and the artist associated with
     *                  the booking, that is needed for the information in the email
     * @param session used to determine if the user is logged in
     * @return redirects back to the booking view after having sent the email for now,
     * will be changed.
     */
    @PostMapping("/send-confirmation-mail")
    public String sendConfirmationEmail(@RequestParam int bookingId, HttpSession session, Model model) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        Booking booking =  bookingService.getBookingDetail(bookingId);
        Client client =booking.getClient();
        client.setEmail("nannahofgaard@hotmail.com");
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
        return "redirect:/booking?bookingId="+ bookingId + "&username=" +username;
    }

}