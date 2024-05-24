package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.repositories.TattooArtistRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.springframework.mail.SimpleMailMessage;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
    public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    TattooArtistRepository tattooArtistRepository;
    @Autowired
    BookingService bookingService;
    @Autowired
    TattooArtistService tattooArtistService;




    public void sendConfirmationMail(int bookingId, String username) {
        Booking booking =  bookingService.getBookingDetail(bookingId);
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        Client client =booking.getClient();
        String bookingEnd =booking.getEndTimeSlot().format(DateTimeFormatter.ofPattern("HH:mm"));
        String bookingStart =booking.getStartTimeSlot().format(DateTimeFormatter.ofPattern("HH:mm"));
        String bookingDate = booking.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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

        String processedHTMLTemplate = templateEngine.process("home/confirmation-mail", context);
        // Start preparing the email
        MimeMessagePreparator preparator = message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(client.getEmail());
            helper.setSubject("Booking Confirmation");
            helper.setText(processedHTMLTemplate, true);
        };

        javaMailSender.send(preparator); //send the email
    }


    /**
     * @author Nanna og Munazzah
     * @param recipient
     * @param subject
     * @param content
     */
    public void sendEmail(String recipient, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    /**
     * @author Munazzah
     * @param email
     * @return boolean
     * @summary Uses regex to check if the structure of the mail is valid fx xxxx@yyy.mmm
     * and if the email is the same that is written in the database
     */
    public boolean isValidEmail(String email, String username) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches() && tattooArtistRepository.getEmail(username).equals(email);
    }

}
