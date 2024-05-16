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
}
