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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    LoginService loginService;
    @Autowired
    BookingService bookingService;

    @Autowired
    TattooArtistService tattooArtistService;

    /**
     * @param model   to add attributes to controller
     * @param session to check if logged in
     * @return String - View of the client-list page
     * @author Munazzah
     * @summary Gets the sorted list of Clients from the service layer, and then uses Map to
     * group the Clients based on the first letter in name
     */
    @GetMapping("/client-list")
    public String clientList(Model model, HttpSession session, @RequestParam(required = false) Integer bookingId, @RequestParam(required = false) LocalDate date) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        List<Client> sortedClients = clientService.getSortedListOfClients();
        // ADT Map is the result here, where the key is a character (first letter) and value is List<Client>
        // Uses TreeMap to maintain the natural order of the keys (that are sorted beforehand)
        // Uses stream to handle everything simultaneously
        // Uses collect (Collectors.groupingBy) to group the elements of the stream based on first letter in first name
        Map<Character, List<Client>> groupedClients = sortedClients.stream()
                .collect(Collectors.groupingBy(client -> client.getFirstName().charAt(0),
                        TreeMap::new, Collectors.toList()));

        model.addAttribute("groupedClients", groupedClients);
        //If creating booking:
        if (bookingId != null) {
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("date", date);
        }

        return "home/client-list";
    }

    /**
     * @param searchQuery
     * @param model
     * @param session
     * @return String - View of search-results
     * @Author Munazzah
     * @summary Search for a Client based on phone number or first name. The if-statement
     * checks if it is a number or name and acts accordingly
     */
    @GetMapping("/search-result")
    public String searchResult(@RequestParam("searchQuery") String searchQuery, Model model, HttpSession session, @RequestParam(required = false) Integer bookingId, @RequestParam(required = false) LocalDate date) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        // Checks (via regex) if there are only numbers or only letters and acts accordingly
        if (searchQuery.matches("[0-9]+")) {
            model.addAttribute("searchType", "phoneNumber");
            List<Client> clientByNumber = clientService.getClientsByPhoneNumber(Integer.parseInt(searchQuery));
            model.addAttribute("results", clientByNumber);
        } else {
            model.addAttribute("searchType", "firstName");
            List<Client> clientByName = clientService.getClientsByFistName(searchQuery);
            model.addAttribute("results", clientByName);
        }
        model.addAttribute("searchQuery", searchQuery);
        //If creating booking:
        if (bookingId != null) {
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("date", date);
        }
        return "home/search-result";
    }

    /**
     * @param searchQuery        to get what there has been searched for
     * @param model              to add attributes to the controller
     * @param redirectAttributes to add redirect message
     * @param session            to check if logged in
     * @return String - View of search-results
     * @author Munazzah
     * @summary Search for a Client based on first name or phone number. The if statement
     * validates that is either one or the other
     */
    @PostMapping("/search-result")
    public String search(@RequestParam("search") String searchQuery, Model model,
                         RedirectAttributes redirectAttributes, HttpSession session, @RequestParam(required = false) Integer bookingId, @RequestParam(required = false) LocalDate date) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        model.addAttribute("searchQuery", searchQuery);
        // Checks (via regex) if there is a mix of numbers and letters. If yes, it redirects with error message
        if (searchQuery.matches(".*[A-Za-z].*") && searchQuery.matches(".*[0-9].*")) {
            redirectAttributes.addFlashAttribute("message", "Please enter a valid number or first name");
            return "redirect:/client-list";
        }
        redirectAttributes.addAttribute("searchQuery", searchQuery);
        //If creating booking:
        if (bookingId != null) {
            return "redirect:/search-result?bookingId=" + bookingId + "&date=" + date;
        }
        return "redirect:/search-result";
    }

    /**
     * Displays the client detail page with an optional delete warning.
     *
     * @param session        the current HTTP session to check if logged in
     * @param model          the model to add attributes to for rendering view
     * @param clientId       the ID of the client to view
     * @param clientToDelete the ID of the client to delete, if applicable
     * @return the view name of the client detail page
     * @author Emma
     */
    @GetMapping("/client")
    public String seeClient(HttpSession session, Model model, @RequestParam("clientId") int clientId,
                            @RequestParam(required = false) Integer clientToDelete) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        if (clientToDelete != null) {
            model.addAttribute("clientToDelete", clientToDelete);
        }

        Client client = clientService.getClientFromClientId(clientId);
        model.addAttribute("client", client);

        List<Booking> clientBookings = bookingService.getBookingsByClientId(clientId);
        model.addAttribute("clientBookings", clientBookings);

        return "home/client";
    }

    /**
     * Redirects to the client detail page with a delete warning.
     *
     * @param clientToDelete     the ID of the client to delete
     * @param clientId           the ID of the client to view
     * @param redirectAttributes the attributes for a redirect scenario
     * @param model              the model to add attributes to for rendering view
     * @param session            the current HTTP session to check if logged in
     * @return the redirect string to the client detail page with a delete warning
     * @author Emma
     */
    @PostMapping("/client")
    public String clientWithWarning(@RequestParam Integer clientToDelete, @RequestParam int clientId, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        Client client = clientService.getClientFromClientId(clientId);
        model.addAttribute("client", client);

        redirectAttributes.addAttribute("clientToDelete", clientToDelete);
        return "redirect:/client?clientId=" + clientId;
    }

    /**
     * Displays the edit client page.
     *
     * @param model    the model to add attributes to for rendering view
     * @param session  the current HTTP session to check if logged in
     * @param clientId the ID of the client to edit
     * @return the view name of the edit client page
     * @author Emma
     */
    @GetMapping("/edit-client")
    public String editClient(Model model, HttpSession session, @RequestParam("clientId") int clientId) {
        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        Client client = clientService.getClientFromClientId(clientId);
        model.addAttribute("client", client);

        return "home/edit-client";
    }

    /**
     * Updates the client information and redirects to the client detail page.
     *
     * @param firstName   the first name of the client
     * @param lastName    the last name of the client
     * @param email       the email of the client
     * @param phoneNumber the phone number of the client
     * @param description the description of the client
     * @param clientId    the ID of the client to update
     * @param model       the model to add attributes to for rendering view
     * @param session     the current HTTP session to check if logged in
     * @return the redirect string to the client detail page
     * @author Emma
     */
    @PostMapping("/edit-client")
    public String updateClient(@RequestParam String firstName, @RequestParam String lastName,
                               @RequestParam String email, @RequestParam int phoneNumber,
                               @RequestParam String description, @RequestParam int clientId,
                               Model model, HttpSession session) {

        loginService.addLoggedInUserInfo(model, session, tattooArtistService);
        clientService.updateClient(firstName, lastName, email, phoneNumber, description, clientId);
        return "redirect:/client?clientId=" + clientId;
    }

    /**
     * Deletes the client information and redirects to the client detail page.
     *
     * @param model    the model to add attributes to for rendering view
     * @param session  the current HTTP session to check if logged in
     * @param clientId the ID of the client to delete
     * @return the redirect string to the client detail page
     * @author Emma
     */
    @PostMapping("/delete-client")
    public String deleteClient(Model model, HttpSession session, @RequestParam("clientId") int clientId) {
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);
        clientService.deleteClientInfoByClientId(clientId);
        return "redirect:/client?clientId=" + clientId;
    }

    /**
     * @param bookingId // * @param clientId
     * @param model
     * @param session   Used to determine if the user is logged in or not. User will be redirected
     *                  to index page if not logged in.
     * @return String - add-client view
     * @Author Tara
     * @Summary Creates the new client, that the user wants to add to the booking that the user is creating
     */
    @GetMapping("/add-client")
    public String addClient(@RequestParam int bookingId, @RequestParam LocalDate date,
                            Model model,
                            HttpSession session) {

        if (!loginService.isUserLoggedIn(session)) {
            return "redirect:/";
        }
        loginService.addLoggedInUserInfo(model, session, tattooArtistService);

        model.addAttribute("date", date); //needed to return to booking page in "back-arrow".
        model.addAttribute("bookingId", bookingId); //needed to return to booking page in "back-arrow".
        return "home/add-client";
    }

    /**
     * @param bookingId   //* @param clientId
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @param description
     * @return
     * @Author Tara
     * @Summary Saves the new client to the booking that the user is creating
     */
    @PostMapping("/add-client")
    public String saveClient(@RequestParam int bookingId,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String email,
                             @RequestParam int phoneNumber,
                             @RequestParam String description) {

        // create new client
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPhoneNumber(phoneNumber);
        client.setDescription(description);
        //Returns the new client so that the clientId can be used to update the default client in the booking
        client = clientService.saveClient(client);
        int clientId = client.getId();

        return "redirect:/booking-preview?bookingId=" + bookingId + "&clientId=" + clientId;
    }

    /**
     * @Author Tara
     * @param model
     * @param session
     * @param bookingId
     * @return
     */
    @GetMapping("/choose-client")
    public String chooseClient(Model model, HttpSession session,
                               @RequestParam int bookingId, @RequestParam LocalDate date) {
        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (loggedIn) {
            model.addAttribute("loggedIn", loggedIn);
        } else {
            return "redirect:/";
        }

        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);
        model.addAttribute("date", date);
        //Værdier som skal videre til view
        //Integer bookingId = (Integer) session.getAttribute("bookingId");
        model.addAttribute("bookingId", bookingId);


        List<Client> sortedClients = clientService.getSortedListOfClients();
        //ADT Map is the result here, where the key is a character (first letter) and value is List<Client>
        //Uses TreeMap to maintain the natural order of the keys (that are sorted beforehand)
        //Uses stream to handle everything simulationaly
        //Uses collect (Collectors.groupingBy) to group the elements of teh stream based on first letter in first name
        Map<Character, List<Client>> groupedClients = sortedClients.stream()
                .collect(Collectors.groupingBy(client -> client.getFirstName().charAt(0),
                        TreeMap::new, Collectors.toList()));

        model.addAttribute("groupedClients", groupedClients);
        return "home/choose-client";
    }

    /**
     * @Author Tara
     * @param session
     * @param searchQuery
     * @param bookingId
     * @param model
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/search-for-existing-client")
    public String searchForExistingClient(HttpSession session,
                                          @RequestParam String searchQuery,
                                          @RequestParam int bookingId,
                                        @RequestParam LocalDate date,
                                        @RequestParam(required = false) int clientId, //clientId er ikke et must*/
                                          Model model,
                         RedirectAttributes redirectAttributes) {
        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (!loggedIn) {
            return "redirect:/";
        }


        String username = (String) session.getAttribute("username");


        return "redirect:/booking-preview?bookingId=" + bookingId + "&clientId=" + clientId;

    }
}

