package com.example.streetinkbookingsystem.repositories;

import com.example.streetinkbookingsystem.models.Client;
import com.example.streetinkbookingsystem.models.TattooArtist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public Client getClientFromClientId(int clientId) {
        String query = "SELECT * FROM client WHERE id = ?";
        RowMapper<Client> rowMapper = new BeanPropertyRowMapper<>(Client.class);
        try {
            return jdbcTemplate.queryForObject(query, rowMapper, clientId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateClient(String firstName, String lastName, String email, int phoneNumber, String description, int clientId) {
        String query = "UPDATE client SET first_name = ?, last_name = ?, email = ?, phone_number = ?, description = ? WHERE id = ?";
        try {
            jdbcTemplate.update(query,
                    firstName,
                  lastName,
                 email,
                    phoneNumber,
                    description,
                   clientId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("something went wrong");
        }
    }

}
