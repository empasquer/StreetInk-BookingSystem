package com.example.streetinkbookingsystem;

import com.example.streetinkbookingsystem.services.CalendarService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalendarServiceTest {
    CalendarService calendarService = new CalendarService();

    @Test
    public void givenLocalDate_returnWeekNumbersForMonth() {
        // Create an instance of CalendarService

        // Call the method on the instance
        int[] actualWeekNumbers = calendarService.getWeekNumbers(LocalDate.of(2024, 1, 1));

        // Define expected week numbers
        int[] expectedWeekNumbers = {1, 2, 3, 4, 5, 6};

        // Assert the result
        assertArrayEquals(expectedWeekNumbers, actualWeekNumbers);
    }

@Test
public void givenLocalDate_returnNumberOfDaysToMonday(){
        int expectedDays=6;
        int actualDays=calendarService.getEmptyStartFills(LocalDate.of(2024,12,1));
        assertEquals(expectedDays, actualDays);
}

@Test
    public void givenLocalDate_returnNumberOfDaysTillEndOFCalendar() {
        int expectedDays = 5;
        int actualDays = calendarService.getEmptyEndFills(LocalDate.of(2024,12,1),calendarService.getDaysInMonth(2024, Month.DECEMBER));
        assertEquals(expectedDays, actualDays);
    }
}
