package com.example;

public enum ColumnNumbers {
    ratingHotel(2), statusTour(9), statusTourist(2), tourTourist(0), hotelTour(11);
    private final int value;

    ColumnNumbers(final int value) {
        this.value = value;
    }
    public int get() {
        return value;
    }
}