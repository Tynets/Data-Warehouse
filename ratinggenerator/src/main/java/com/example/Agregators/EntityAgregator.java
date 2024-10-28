package com.example.Agregators;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class EntityAgregator<K, T> {
    @Getter
    private final Map<K, T> entities;

    public void put(K id, T hotel) {
        entities.put(id, hotel);
    }
}