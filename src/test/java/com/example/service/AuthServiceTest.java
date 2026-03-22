package com.example.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    @Test
    void testLogin() {
        AuthService service = new AuthService();
        assertNotNull(service.login("user", "pass"));
    }
}
