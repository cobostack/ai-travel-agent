package com.example.service;

import com.example.entity.Item;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ItemService {
    private Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;
    
    public Item create(Item item) {
        item.setId(nextId++);
        items.put(item.getId(), item);
        return item;
    }
    
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }
}
