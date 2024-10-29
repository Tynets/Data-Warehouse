package com.example.Containers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Parameters {
    private int nHotels = 100;
    private int nTransp = 100;
    private int nClients = 100;
    private int nTours = 1000;
    private int startYear = 2015;
    private int endYear = 2020;
    private Boolean saveAggregator = false;
    private Boolean loadAggregator = false;
    @Getter
    private static final int divisibleBy = 10;
    private static int getNumber(String line) {
        int number = 0;
        try {
            String[] parts = line.split(" ");
            number = Integer.valueOf(parts[1]);
            if (number % divisibleBy != 0) {
                while (number % divisibleBy != 0) number++;
                System.out.println(String.format("%s is not divisible by %d, setting to %d", parts[0], divisibleBy, number));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }
    public static Parameters getParameters() {
        BufferedReader reader = null;
        Parameters parameters = new Parameters();
        try {
            reader = new BufferedReader(new FileReader("config.config"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return parameters;
        }
        try {
            String line = reader.readLine();
            parameters.setNHotels(getNumber(line));
            line = reader.readLine();
            parameters.setNTransp(getNumber(line));
            line = reader.readLine();
            parameters.setNClients(getNumber(line));
            line = reader.readLine();
            parameters.setNTours(getNumber(line));
            line = reader.readLine();
            parameters.setStartYear(Integer.valueOf(line.split(" ")[1]));
            line = reader.readLine();
            parameters.setEndYear(Integer.valueOf(line.split(" ")[1]));
            line = reader.readLine();
            parameters.setSaveAggregator(Boolean.valueOf(line.split(" ")[1]));
            line = reader.readLine();
            parameters.setLoadAggregator(Boolean.valueOf(line.split(" ")[1]));
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameters;
    }
}
