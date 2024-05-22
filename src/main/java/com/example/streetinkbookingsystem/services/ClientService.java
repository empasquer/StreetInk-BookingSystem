package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
     * @summary Takes the Arraylist from the repository, sorts it using Collections.sort() and
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

    public Client getClientFromClientId(int clientId) {
        return clientRepository.getClientFromClientId(clientId);
    }

    public void updateClient(String firstName, String lastName, String email, int phoneNumber, String description,
                             int clientId ) {
        clientRepository.updateClient(firstName, lastName,email, phoneNumber, description, clientId);
    }

    public void deleteClientInfoByClientId(int clientId) {
        clientRepository.updateClient("Unknown", null, "unknown",
                0, null, clientId);
    }

    /**
     * @author Munazzah
     * @summary Is a scheduled method that runs every day at midnight, where it finds clients
     * that havent had any booking in the past 5 years, adds them to a list, and then inactivates
     * the clients, do their information are overwritten with the default client
     * The cron is in sec, minutes, hours, day, month, day of week
     */
    @Scheduled(cron = "0 04 21 * * ?") //Runs every day at midnight
    public void cleanupInactiveClients() {
        List<Client> inactiveClientIds = clientRepository.findInactivateClients();
        if (inactiveClientIds != null) {
            for (Client client : inactiveClientIds) {
                updateClient("Unknown", null, "unknown", 0, null, client.getId());
            }
        }
    }
}
