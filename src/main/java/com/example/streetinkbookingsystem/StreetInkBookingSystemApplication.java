package com.example.streetinkbookingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.streetinkbookingsystem")
//Scans for configurations and stuff in all packages, so that the configuration can happen
public class StreetInkBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreetInkBookingSystemApplication.class, args);
    }

}
