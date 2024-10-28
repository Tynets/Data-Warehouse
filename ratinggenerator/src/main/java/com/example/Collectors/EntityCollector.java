package com.example.Collectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.example.Agregators.EntityAgregator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityCollector implements Runnable {
    private final EntityAgregator<String, String[]> agregator;
    private final String entitiesFile;

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(new File(entitiesFile))) {
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] split = line.split("\\|");
                agregator.put(split[0], split);
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }
}