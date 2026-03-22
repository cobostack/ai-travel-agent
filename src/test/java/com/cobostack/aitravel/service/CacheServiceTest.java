package com.cobostack.aitravel.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CacheServiceTest {
    private final CacheService service = new CacheService();
    
    @Test
    void testClearCache() {
        assertDoesNotThrow(() -> service.clearCache());
    }
}
