package com.example.streetinkbookingsystem.models;

import java.time.LocalDate;

public class Booking {
    LocalDate date;

    public Booking() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
