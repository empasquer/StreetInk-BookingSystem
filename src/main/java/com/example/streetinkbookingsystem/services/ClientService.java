package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
