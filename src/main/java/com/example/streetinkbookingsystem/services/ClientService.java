package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

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

    @Scheduled(cron = "0 0 0 * * ?") //Runs every day at midnight, checks for inactive clients and update to unknown
    public void cleanupInactiveClients() {
        List<Client> inactiveClientIds = clientRepository.findInactivateClients();
        if (inactiveClientIds != null) {
            for (Client client : inactiveClientIds) {
                updateClient("Unknown", null, "unknown", 0, null, client.getId());
            }
        }
    }
}
