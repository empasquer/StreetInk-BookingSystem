package com.example.streetinkbookingsystem.models;

import java.time.LocalDate;
<<<<<<< HEAD
import java.time.LocalTime;

public class Booking {
    private int id;
    private LocalTime startTimeSlot;
    private LocalTime endTimeSlot;
    private LocalDate date;
    private int clientId;
    private String username;
    private String projectTitle;
    private String projectDesc;
    private String personalNote;
    private boolean isDepositPayed;
=======

import java.time.LocalTime;

import java.util.ArrayList;

public class Booking {
    private int id; //ved endnu ikke om denne skal bruges.
    private LocalTime startTimeSlot;
    private LocalTime endTimeSlot;
    private LocalDate date;
    private Client client;
    private String projectTitle;
    private String projectDesc;
    private String personalNote;
    private Boolean isDepositPayed;
    private ArrayList<Byte[]> projectPictures;
    //Overvej senere om denne skal ændres til string, med billedestier.


>>>>>>> db1b8f5d72bbfcb46789182717b6ed2dd6b52651

    public Booking() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

<<<<<<< HEAD
=======

>>>>>>> db1b8f5d72bbfcb46789182717b6ed2dd6b52651
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

<<<<<<< HEAD
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
=======

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
>>>>>>> db1b8f5d72bbfcb46789182717b6ed2dd6b52651
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

<<<<<<< HEAD
    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }

    public boolean isDepositPayed() {
        return isDepositPayed;
    }

    public void setDepositPayed(boolean depositPayed) {
        isDepositPayed = depositPayed;
    }
=======

    public String getPersonalNote() {
        return personalNote;
    }

    public void setPersonalNote(String personalNote) {
        this.personalNote = personalNote;
    }

    public Boolean getDepositPayed() {
        return isDepositPayed;
    }

    public void setDepositPayed(Boolean depositPayed) {
        isDepositPayed = depositPayed;
    }

    public ArrayList<Byte[]> getProjectPictures() {
        return projectPictures;
    }

    public void setProjectPictures(ArrayList<Byte[]> projectPictures) {
        this.projectPictures = projectPictures;
    }

>>>>>>> db1b8f5d72bbfcb46789182717b6ed2dd6b52651
}
