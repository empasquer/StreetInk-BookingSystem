package com.example.streetinkbookingsystem.controllers;

import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.TattooArtist;
import com.example.streetinkbookingsystem.services.ClientService;
import com.example.streetinkbookingsystem.services.LoginService;
import com.example.streetinkbookingsystem.services.TattooArtistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    TattooArtistService tattooArtistService;


    /**
     * @author Munazzah
     * @param model
     * @param session
     * @return String - View of the client-list page
     * @summary Gets the sorted list og Clients from the service layer, and then uses Map to
     * group the Clients based on the first letter in name
     */
    @GetMapping("/client-list")
    public String clientList(Model model, HttpSession session) {
        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (loggedIn) {
            model.addAttribute("loggedIn", loggedIn);
        } else {
            return "redirect:/";
        }

        String username = (String) session.getAttribute("username");
        TattooArtist tattooArtist = tattooArtistService.getTattooArtistByUsername(username);
        model.addAttribute("tattooArtist", tattooArtist);

        List<Client> sortedClients = clientService.getSortedListOfClients();
        //ADT Map is the result, where the key is a character (first letter) and value is List<Client>
        //Uses TreeMap to maintain the natural order of the keys (that are sorted beforehand)
        //Uses stream to handle everything simulationaly
        //Uses collect (Collectors.groupingBy) to group the elements of teh stream based on first letter in first name
        Map<Character, List<Client>> groupedClients = sortedClients.stream()
                .collect(Collectors.groupingBy(client -> client.getFirstName().charAt(0),
                        TreeMap::new, Collectors.toList()));

        model.addAttribute("groupedClients", groupedClients);
        return "home/client-list";
    }

    /**
     * @author Muanzzah
     * @param searchQuery
     * @param model
     * @param redirectAttributes
     * @param session
     * @return String - View of search-results
     * @summary Search for a Client based on phone number or first name. The if-statement
     * checks if it is a number or name and acts accordingly
     */
    @PostMapping("/search")
    public String search(@RequestParam("search") String searchQuery, Model model,
                         RedirectAttributes redirectAttributes, HttpSession session) {
        boolean loggedIn = loginService.isUserLoggedIn(session);
        if (loggedIn) {
            model.addAttribute("loggedIn", loggedIn);
        } else {
            return "redirect:/";
        }

        model.addAttribute("searchQuery", searchQuery);
        //Checks (via regex) if there are only numbers, letters or a mix of both and acts accordingly
        if (searchQuery.matches("[0-9]+")) {
            model.addAttribute("searchType", "phoneNumber");
            List<Client> clientByNumber = clientService.getClientsByPhoneNumber(Integer.parseInt(searchQuery));
            model.addAttribute("results", clientByNumber);
        } else if (searchQuery.matches("[A-Za-z]+")) {
            model.addAttribute("searchType", "firstName");
            List<Client> clientByName = clientService.getClientsByFistName(searchQuery);
            model.addAttribute("results", clientByName);
        } else {
            redirectAttributes.addFlashAttribute("message", "Please enter a valid number or first name");
            return "redirect:/client-list";
        }
        return "home/search-result";
    }

}