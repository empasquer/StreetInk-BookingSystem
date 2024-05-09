package com.example.streetinkbookingsystem.services;

import com.example.streetinkbookingsystem.models.Calendar;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
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

    // Method to get week-numbers of a month
    public  int[] getWeekNumbers(LocalDate firstDateInMonth){
         int[] weekNumbers = new int[6];
        int weekNumber = firstDateInMonth.get(WeekFields.ISO.weekOfWeekBasedYear());
        for (int i=0; i< 6; i++  ){
            weekNumbers[i]= weekNumber;
            weekNumber +=1;
        }
        return weekNumbers;
    }

    // Method to find number of empty slots before first day in month
    public int getEmptyStartFills(LocalDate firstDateInMonth) {
        int firstDay = firstDateInMonth.getDayOfWeek().getValue();
        System.out.println(firstDay);
        int fillers = firstDay - 1; // Subtract 1 because we want Monday to have 0 fillers
        return fillers;
    }


    public int getEmptyEndFills(LocalDate firstDateInMonth, ArrayList daysInMonth) {
        int startFillers = getEmptyStartFills(firstDateInMonth);
        System.out.println(startFillers);
        int endFillers = 42 - startFillers - daysInMonth.size();
        System.out.println(endFillers);
        return endFillers;

    }
}

