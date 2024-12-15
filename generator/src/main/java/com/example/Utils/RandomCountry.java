package com.example.Utils;

import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.ArrayUtils;

public class RandomCountry {
    private static final String countries[] = {"Greece", "Turkey", "Spain", "Croatia", "Thailand"};
    private static final String cities[][] = {{"Molyvos", "Preveza"}, {"Olu Deniz", "Dalyan"}, {"Malaga", "Ibiza Town"}, {"Rovinj", "Bol"}, {"Phuket", "Koh Samet"}};
    private static final int hotelPriceBounds[][][] = {{{500, 1000}, {800, 1200}}, {{1000, 2000}, {800, 1000}}, {{1200, 2100}, {2000, 2500}}, {{600, 900}, {500, 800}}, {{2000, 2500}, {1800, 1900}}};
    private static final int transportationPriceBounds[][] = {{100, 450}, {500, 1000}, {250, 600}, {190, 500}, {500, 1000}};
    public static String randomCountry() {
        return countries[ThreadLocalRandom.current().nextInt(countries.length)];
    }
    public static String randomCity(String country) {
        int countryIdx = ArrayUtils.indexOf(countries, country);
        return cities[countryIdx][ThreadLocalRandom.current().nextInt(cities[countryIdx].length)];
    }
    public static int[] hotelPriceBounds(String country, String city) {
        int countryIdx = ArrayUtils.indexOf(countries, country);
        int cityIdx = ArrayUtils.indexOf(cities[countryIdx], city);
        return hotelPriceBounds[countryIdx][cityIdx];
    }
    public static int[] transportationPriceBounds(String country) {
        int countryIdx = ArrayUtils.indexOf(countries, country);
        return transportationPriceBounds[countryIdx];
    }
}