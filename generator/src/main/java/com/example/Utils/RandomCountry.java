package com.example.Utils;

import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.ArrayUtils;

public class RandomCountry {
    private static final String countries[] = {"Greece", "Turkey", "Spain", "Croatia", "Thailand"};
    private static final String cities[][] = {{"Molyvos", "Preveza"}, {"Olu Deniz", "Dalyan"}, {"Malaga", "Ibiza Town"}, {"Rovinj", "Bol"}, {"Phuket", "Koh Samet"}};
    private static final int hotelPriceBounds[][] = {{500, 1000}, {200, 1000}, {1200, 2100}, {700, 1500}, {900, 2000}};
    private static final int transportationPriceBounds[][] = {{100, 450}, {50, 300}, {250, 600}, {190, 500}, {150, 500}};
    public static String randomCountry() {
        return countries[ThreadLocalRandom.current().nextInt(countries.length)];
    }
    public static String randomCity(String country) {
        int countryIdx = ArrayUtils.indexOf(countries, country);
        return cities[countryIdx][ThreadLocalRandom.current().nextInt(cities[countryIdx].length)];
    }
    public static int[] hotelPriceBounds(String country) {
        int countryIdx = ArrayUtils.indexOf(countries, country);
        return hotelPriceBounds[countryIdx];
    }
    public static int[] transportationPriceBounds(String country) {
        int countryIdx = ArrayUtils.indexOf(countries, country);
        return transportationPriceBounds[countryIdx];
    }
}