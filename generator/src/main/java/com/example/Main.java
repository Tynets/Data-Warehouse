package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.example.Containers.Aggregator;
import com.example.Containers.Parameters;
import com.example.Containers.WriterQueue;
import com.example.Generators.ClientGenerator;
import com.example.Generators.HotelGenerator;
import com.example.Generators.TourGenerator;
import com.example.Generators.TransporterGenerator;
import com.example.Utils.ThreadWriter;

public class Main {
    public static void main(String[] args) {
        Parameters parameters = Parameters.getParameters();
        long start = System.currentTimeMillis();
        Aggregator aggregator = new Aggregator();
        if (parameters.getLoadAggregator()) aggregator.deserialize();
        WriterQueue queue = new WriterQueue();
        ExecutorService generators = Executors.newFixedThreadPool(50);
        ExecutorService writers = Executors.newFixedThreadPool(7);
        for (int j = 0; j < parameters.getNHotels() / Parameters.getDivisibleBy(); j++) {
            generators.submit(new HotelGenerator(Parameters.getDivisibleBy(), aggregator, queue));
        }
        for (int j = 0; j < parameters.getNTransp() / Parameters.getDivisibleBy(); j++) {
            generators.submit(new TransporterGenerator(Parameters.getDivisibleBy(), aggregator, queue));
        }
        for (int j = 0; j < parameters.getNClients() / Parameters.getDivisibleBy(); j++) {
            generators.submit(new ClientGenerator(Parameters.getDivisibleBy(), aggregator, queue));
        }
        writers.submit(new Thread(new ThreadWriter(queue, "Hotel", "Hotels", parameters.getLoadAggregator())));
        writers.submit(new Thread(new ThreadWriter(queue, "Transp", "Transporters", parameters.getLoadAggregator())));
        writers.submit(new Thread(new ThreadWriter(queue, "Client", "Clients", parameters.getLoadAggregator())));
        writers.submit(new Thread(new ThreadWriter(queue, "Tour", "Tours", parameters.getLoadAggregator())));
        writers.submit(new Thread(new ThreadWriter(queue, "Reserv", "Reservations", parameters.getLoadAggregator())));
        writers.submit(new Thread(new ThreadWriter(queue, "Transo", "Transportations", parameters.getLoadAggregator())));
        writers.submit(new Thread(new ThreadWriter(queue, "Tourist", "Tourists", parameters.getLoadAggregator())));
        writers.shutdown();
        generators.shutdown();
        try {
            generators.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ExecutorService tours = Executors.newFixedThreadPool(50);
        for (int j = 0; j < parameters.getNTours() / Parameters.getDivisibleBy(); j++) {
            tours.submit(new TourGenerator(Parameters.getDivisibleBy(), parameters.getStartYear(), parameters.getEndYear(), aggregator, queue));
        }
        tours.shutdown();
        try {
            tours.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writers.shutdownNow();
        try {
            writers.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (parameters.getSaveAggregator()) aggregator.serialize();
        long end = System.currentTimeMillis();
        System.out.println(String.format("Finished in %.2fs", ((float)(end - start) / 1000)));
        return;
    }
}