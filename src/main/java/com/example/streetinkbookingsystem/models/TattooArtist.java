package com.example.streetinkbookingsystem.models;

import org.springframework.jdbc.core.JdbcTemplate;

public class TattooArtist extends Person {
    private String username;
    private String password;
    private Byte profilePicture;
    private String facebookUrl;
    private String instragramUrl;
    private int avgWorkHours;
    private JdbcTemplate jdbcTemplate;

    public TattooArtist(){
        this.jdbcTemplate = new JdbcTemplate();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Byte profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstragramUrl() {
        return instragramUrl;
    }

    public void setInstragramUrl(String instragramUrl) {
        this.instragramUrl = instragramUrl;
    }

    public int getAvgWorkHours() {
        return avgWorkHours;
    }

    public void setAvgWorkHours(int avgWorkHours) {
        this.avgWorkHours = avgWorkHours;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
