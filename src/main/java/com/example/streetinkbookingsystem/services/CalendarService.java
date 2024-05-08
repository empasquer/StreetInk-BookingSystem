package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Calendar;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

@Service
public class CalendarService {

    public LocalDate getCurrentDate(){
        Calendar calendar = new Calendar();
        calendar.initializeWithCurrentDate();
        return LocalDate.of(calendar.getYear(),calendar.getMonth(),calendar.getDay());
    }


    // Method to get all days in a month
    public ArrayList<LocalDate> getDaysInMonth(int year, Month month) {
        ArrayList<LocalDate> daysInMonth = new ArrayList<>();
        LocalDate firstDate = LocalDate.of(year, month, 1); // Start from the first day of the month

        while (firstDate.getMonth() == month) {
            daysInMonth.add(firstDate); // Add the current date to the list
            firstDate = firstDate.plusDays(1); // Move to the next day
        }
        return daysInMonth;
    }


}

