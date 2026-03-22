package com.cobostack.aitravel.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @CacheEvict(value = "travelPlans", allEntries = true)
    public void clearCache() {
    }
}
