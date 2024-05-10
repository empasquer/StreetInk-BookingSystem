package com.example.streetinkbookingsystem.models;

import java.time.LocalDate;

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
    private String privateNote;
    private Boolean isDepositPayed;
    private ArrayList<Byte[]> projectPictures;
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


    public String getPrivateNote() {
        return privateNote;
    }

    public void setPrivateNote(String privateNote) {
        this.privateNote = privateNote;
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

}
