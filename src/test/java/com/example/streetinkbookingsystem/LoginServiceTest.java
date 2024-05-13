package com.example.streetinkbookingsystem;

import com.example.streetinkbookingsystem.services.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    LoginService loginService = new LoginService();

    @Test
    void testHashPassword() {
        // Test password hashing
        String password = "password";
        String hashedPassword = loginService.hashPassword(password);
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword); // Ensure password is hashed

        // Test password verification
        assertTrue(loginService.verifyPassword(password, hashedPassword));
        assertFalse(loginService.verifyPassword("wrongPassword", hashedPassword));
    }
}
