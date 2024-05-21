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
    public Client getClientFromClientId(int clientId) {
        String query = "SELECT * FROM client WHERE id = ?";
        RowMapper<Client> rowMapper = new BeanPropertyRowMapper<>(Client.class);
            return jdbcTemplate.queryForObject(query, rowMapper, clientId);
    }
    /**
     * @author Munazzah
     * @param phoneNumber
     * @return List of Clients
     */
    public List<Client> getClientsByPhoneNumber(int phoneNumber) {
        String query = "SELECT * FROM client WHERE phone_number LIKE ?";
        RowMapper<Client> rowMapper = new BeanPropertyRowMapper<>(Client.class);
        try {
            return jdbcTemplate.query(query, rowMapper, "%" + phoneNumber + "%");
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * @author Munazzah
     * @param firstname
     * @return List of clients
     */
    public List<Client> getClientsByFirstName(String firstname) {
        String query = "SELECT * FROM client WHERE first_name LIKE ?";
        RowMapper<Client> rowMapper = new BeanPropertyRowMapper<>(Client.class);
        try {
            return jdbcTemplate.query(query, rowMapper, "%" + firstname + "%");
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

    /**
     * @author Munazzah
     * @return list of clients
     * @summary Checks if any clients' booking date was over 5 years ago, if yes,
     * then adds them to the list
     */
    public List<Client> findInactivateClients() {
        String query = "SELECT c.* FROM Client c LEFT JOIN Booking b ON c.id = b.client_id " +
                "GROUP BY c.id " +
                "HAVING MAX(b.date) < (CURRENT_DATE - INTERVAL 5 YEAR)";

        RowMapper<Client> rowMapper = new BeanPropertyRowMapper(Client.class);

        try {
            return jdbcTemplate.query(query, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * @author Munazzah
     * @return Arraylist of Client
     */
    //Don't know if I can do it like this, or if I should use collections.sort separately?
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

    /**
     * @author Munazzah
     * @return ArrayList of Client
     */
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
