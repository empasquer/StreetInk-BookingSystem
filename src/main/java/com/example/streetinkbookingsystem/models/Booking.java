package com.example.streetinkbookingsystem.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private int id;
    private LocalTime startTimeSlot;
    private LocalTime endTimeSlot;
    private LocalDate date;
    private String username; // Tara tilføjet denne.
    private Client client;
    private String projectTitle;
    private String projectDesc;
    private String personalNote;
    private Boolean isDepositPayed;
    private List<ProjectPicture> projectPictures; // Tara tilføjet denne
   // private ArrayList<Byte[]> projectPictures; Ovenover har overtaget
    //Overvej senere om denne skal ændres til string, med billedestier.


    public Booking() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getStartTimeSlot() {
        return startTimeSlot;
    }

    public void setStartTimeSlot(LocalTime startTimeSlot) {
        this.startTimeSlot = startTimeSlot;
    }

    public LocalTime getEndTimeSlot() {
        return endTimeSlot;
    }

    public void setEndTimeSlot(LocalTime endTimeSlot) {
        this.endTimeSlot = endTimeSlot;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUsername() { // Tara tilføjet denne.
        return username;
    }

    public void setUsername(String username) { // Tara tilføjet denne.
        this.username = username;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }
    // ændret disse. Forvirrede mig med navneskiftet, håber det er ok?
    public Boolean getIsDepositPayed() {
        return isDepositPayed;
    }
    // ændret disse. Forvirrede mig med navneskiftet, håber det er ok?
    public void setIsDepositPayed(Boolean isDepositPayed) {
        this.isDepositPayed = isDepositPayed;
    }

    public List<ProjectPicture> getProjectPictures() {
        return projectPictures;
    }

    public void setProjectPictures(List<ProjectPicture> projectPictures) {
        this.projectPictures = projectPictures;
    }
}
