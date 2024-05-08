package com.example.streetinkbookingsystem.models;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class Calendar {

    private int year;
    private Month month;
    private int day;

    // Default constructor
    public Calendar() {
        // Initialize with default values
    }

    // Getters
    public int getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    // Setters
    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    // Method to initialize with the current date
    public void initializeWithCurrentDate() {
        LocalDate now = LocalDate.now();
        this.year = now.getYear();
        this.month = now.getMonth();
        this.day = now.getDayOfMonth();
    }


}
