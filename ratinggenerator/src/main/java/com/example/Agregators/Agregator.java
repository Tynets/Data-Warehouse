package com.example.Agregators;

import java.util.Deque;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Agregator<K, T> {
    @Getter
    private final Deque<T> ratings;
    private final Map<K, T> hotels;
    private final Map<K, T> tours;
    
    public void put(T split) {
        ratings.addLast(split);
    }
    public T getHotel(K hotelID) {
        return hotels.get(hotelID);
    }
    public T getTour(K tourID) {
        return tours.get(tourID);
    }
}