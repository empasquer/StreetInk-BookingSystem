package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     * @author Munazzah
     * @return ArrayList of Clients
     * @summary Takes the Arraylist for the repository, sorts it using Collections.sort() and
     * returns the sorted list
     */
    public ArrayList<Client> getSortedListOfClients() {
        ArrayList<Client> clients = clientRepository.getListOfClients();
        Collections.sort(clientRepository.getListOfClients());
        ArrayList<Client> sortedList = new ArrayList<>(clients);
        return sortedList;
    }

    /**
     * @author Munazzah
     * @param phoneNumber
     * @return Client
     */
    public List<Client> getClientsByPhoneNumber(int phoneNumber) {
        return clientRepository.getClientsByPhoneNumber(phoneNumber);
    }

    /**
     * @author Munazzah
     * @param firstname
     * @return List of clients
     */
    public List<Client> getClientsByFistName(String firstname) {
        return clientRepository.getClientsByFirstName(firstname);
    }
}
