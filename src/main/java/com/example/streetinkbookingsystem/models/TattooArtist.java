package com.example.streetinkbookingsystem.models;

import java.util.Base64;

public class TattooArtist extends Person {
    private String username;
    private String password;
    private byte[] profilePicture; // ændret til byte array.
    // alm. byte kan åbenbart ikke bruge Java's Base64-klasse til dette formål,
    //hvis den skal kunne samarbejde med database og thymeleaf.
    private String base64ProfilePicture; // Add this field
    private String facebook;
    private String instagram;
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
        if (profilePicture != null) {
            setBase64ProfilePicture(Base64.getEncoder().encodeToString(profilePicture));
        } else {
            setBase64ProfilePicture(null);
        }
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
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


    // Getters and setters for new field
    public String getBase64ProfilePicture() {
        return base64ProfilePicture;
    }

    public void setBase64ProfilePicture(String base64ProfilePicture) {
        this.base64ProfilePicture = base64ProfilePicture;
    }
}
