package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.Agregators.Agregator;
import com.example.Agregators.EntityAgregator;
import com.example.Collectors.EntityCollector;
import com.example.Collectors.RatingGenerator;

public class Main {
    public static void main(String[] args) {
        String hFile = null;
        String tFile = null;
        String tsFile = null;
        String tsNFile = null;
        String rFile = null;
        String rNFile = null;
        try (Scanner scanner = new Scanner(new File("config.config"))) {
            scanner.useDelimiter("\n");
            hFile = scanner.nextLine();
            tFile = scanner.nextLine();
            tsFile = scanner.nextLine();
            tsNFile = scanner.nextLine();
            rFile = scanner.nextLine();
            rNFile = scanner.nextLine();
        } catch (FileNotFoundException e1) { e1.printStackTrace(); }

        ExecutorService collectorEx = Executors.newFixedThreadPool(2);
        EntityAgregator<String, String[]> hotelsA = new EntityAgregator<>(new HashMap<>());
        EntityAgregator<String, String[]> toursA = new EntityAgregator<>(new HashMap<>());
        long st = System.currentTimeMillis();
        collectorEx.submit(new EntityCollector(hotelsA, hFile));
        collectorEx.submit(new EntityCollector(toursA, tFile));
        collectorEx.shutdown();
        try {
            collectorEx.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) { e.printStackTrace(); }

        ExecutorService service = Executors.newFixedThreadPool(2);
        Agregator<String, String[]> agregator = new Agregator<>(new ArrayDeque<>(), hotelsA.getEntities(), toursA.getEntities());
        Agregator<String, String[]> agregatorN = new Agregator<>(new ArrayDeque<>(), hotelsA.getEntities(), toursA.getEntities());
        service.submit(new RatingGenerator(agregator, tsFile));
        service.submit(new RatingGenerator(agregatorN, tsNFile));
        service.shutdown();
        try {
            service.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) { e.printStackTrace(); }

        String[] split = rFile.split("\\.");
        String[] splitN = rNFile.split("\\.");
        ExecutorService dumpEx = Executors.newFixedThreadPool(2);
        dumpEx.submit(new Dumper(agregator.getRatings(), split[1], rFile));
        for (String[] r : agregator.getRatings()) {
            agregatorN.getRatings().addLast(r);
        }
        dumpEx.submit(new Dumper(agregatorN.getRatings(), splitN[1], rNFile));
        dumpEx.shutdown();
        try {
            dumpEx.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println(System.currentTimeMillis() - st);
    }
}