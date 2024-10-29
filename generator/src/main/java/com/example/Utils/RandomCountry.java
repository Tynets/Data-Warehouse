package com.example.Utils;

import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.ArrayUtils;

public class RandomCountry {
    private static final String countries[] = {"Greece", "Turkey", "Spain", "Croatia", "Thailand"};
    private static final String cities[][] = {{"Molyvos", "Preveza"}, {"Olu Deniz", "Dalyan"}, {"Malaga", "Ibiza Town"}, {"Rovinj", "Bol"}, {"Phuket", "Koh Samet"}};
    public static String[] getCountries() {
        return countries;
    }
    public static String[][] getCities() {
        return cities;
    }
    public static String randomCountry() {
        return countries[ThreadLocalRandom.current().nextInt(countries.length)];
    }
    public static String randomCity(String country) {
        int countryIdx = ArrayUtils.indexOf(countries, country);
        return cities[countryIdx][ThreadLocalRandom.current().nextInt(cities[countryIdx].length)];
    }
}