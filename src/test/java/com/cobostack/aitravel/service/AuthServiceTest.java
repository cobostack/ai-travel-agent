package com.cobostack.aitravel.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private final AuthService service = new AuthService();
    
    @Test
    void testLogin() {
        assertNotNull(service.login("user", "pass"));
    }
}
