package com.example.streetinkbookingsystem;

import com.example.streetinkbookingsystem.models.Booking;
import com.example.streetinkbookingsystem.services.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DashboardServiceTest {

    DashboardService dashboardService = new DashboardService();

    @Test
    public void givenBookingList_returnTotalDurationInHours() {
        ArrayList<Booking> bookings = new ArrayList<Booking>();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        booking1.setStartTimeSlot(LocalTime.parse("10:00:00"));
        booking1.setEndTimeSlot(LocalTime.parse("12:00:00"));
        booking2.setStartTimeSlot(LocalTime.parse("12:00:00"));
        booking2.setEndTimeSlot(LocalTime.parse("15:00:00"));
        bookings.add(booking1);
        bookings.add(booking2);
        int actualOutput = dashboardService.calculateTotalBookedHours(bookings);
        int expectedOutput = 5;
        assertEquals(actualOutput,expectedOutput);
    }
}
