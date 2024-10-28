package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        int threads = 0;
        String hFile = null;
        String tFile = null;
        String tsFile = null;
        String rFile = null;
        try (Scanner scanner = new Scanner(new File("config.config"))) {
            scanner.useDelimiter("\n");
            threads = Integer.parseInt(scanner.nextLine());
            hFile = scanner.nextLine();
            tFile = scanner.nextLine();
            tsFile = scanner.nextLine();
            rFile = scanner.nextLine();
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

        long lower = 0;
        long upper = 0;
        long range = (upper - lower + 1) / threads;
        try {
            upper = Files.lines(Paths.get(tsFile)).count();
        } catch (IOException e) { e.printStackTrace(); }
        ExecutorService service = Executors.newFixedThreadPool(threads);
        Agregator<String, String[]> agregator = new Agregator<>(new ArrayDeque<>(), hotelsA.getEntities(), toursA.getEntities());
        for (int i = 0; i < threads; i++) {
            long upperLocal = 0;
            if (i != threads - 1) upperLocal = lower + range - 1;
            else upperLocal = upper;
            service.submit(new RatingGenerator(agregator, lower, upperLocal, tsFile));
            lower += range;
        }
        service.shutdown();
        try {
            service.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) { e.printStackTrace(); }

        Dumper dumper = new Dumper(agregator.getRatings(), rFile);
        String[] split = rFile.split("\\.");
        if (split[1].equals("xlsx")) dumper.dumpXLSX();
        else dumper.dumpCSV();
        System.out.println(System.currentTimeMillis() - st);
    }
}