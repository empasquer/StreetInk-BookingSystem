package com.example.streetinkbookingsystem.models;

import org.springframework.jdbc.core.JdbcTemplate;

public class TattooArtist extends Person {
    private String username;
    private String password;
    private byte[] profilePicture; // ændret til byte array.
    // alm. byte kan åbenbart ikke bruge Java's Base64-klasse til dette formål,
    //hvis den skal kunne samarbejde med database og thymeleaf.
    // private Byte profilePicture; ** gamle attribute
    private String facebookUrl;
    private String instragramUrl;
    private int avgWorkHours;
    private Boolean isAdmin;

    public TattooArtist() {
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
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

    public Boolean getIsAdmin() {
        return isAdmin.booleanValue();
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
