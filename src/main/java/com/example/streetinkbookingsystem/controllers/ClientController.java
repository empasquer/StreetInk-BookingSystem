package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.BookingService;
import com.example.streetinkbookingsystem.services.ClientService;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ClientController {
    @Autowired
    TattooArtistService tattooArtistService;
    @Autowired
    LoginService loginService;
    @Autowired
    ClientService clientService;
    @Autowired
    BookingService bookingService;

    @GetMapping("/client")
    public String seeClient(HttpSession session, Model model, @RequestParam("clientId") int clientId) {
        boolean loggedIn = loginService.isUserLoggedIn(session);

        if (!loggedIn) {
            return "redirect:/";
        }

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        Client client = clientService.getClientFromClientId(clientId);

        List<Booking> clientBookings = bookingService.getBookingsByClientId(clientId);
        model.addAttribute("clientBookings", clientBookings);

        /*
            WHAT TO DO IF NO CLIENT/WRONG CLIENT ID
            if (client == null) {
                return "/home/client-list";
        }*/

        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("client", client);

        return "home/client";
    }

}
