package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public Client getClientByPhoneNumber(int phoneNumber) {
        String query = "SELECT * FROM client WHERE phone_number = ?";
        RowMapper<Client> rowMapper = new BeanPropertyRowMapper<>(Client.class);
        try {
            return jdbcTemplate.queryForObject(query, rowMapper, phoneNumber);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //Don't know if I can do it like this, or if I should use collections.sort separately?
    //Using this for now
    public ArrayList<Client> getAllClientsSortedByFirstName() {
        String query = "SELECT * FROM client ORDER BY first_name";
        RowMapper<Client> rowMapper = new BeanPropertyRowMapper<>(Client.class);
        try {
            List<Client> clients = jdbcTemplate.query(query, rowMapper);
            return new ArrayList<>(clients);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>(); //Empty list if no clients are found
        }
    }

    public ArrayList<Client> getListOfClients() {
        String query = "SELECT * FROM client";
        RowMapper<Client> rowMapper = new BeanPropertyRowMapper<>(Client.class);
        try {
            List<Client> clients = jdbcTemplate.query(query, rowMapper);
            return new ArrayList<>(clients);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>(); //Empty list if no clients are found
        }
    }




}