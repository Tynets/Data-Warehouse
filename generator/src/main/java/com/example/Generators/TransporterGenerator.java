package com.example.Generators;

import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;
import com.example.Containers.Aggregator;
import com.example.Containers.WriterQueue;
import com.example.Utils.AddQuotMarks;
import com.example.Utils.RandomCountry;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransporterGenerator implements Runnable {
    private int numOfTransp;
    private Aggregator aggregator;
    private WriterQueue queue;
    private final String[] transpType = {"Plane", "Bus"};
    @Override
    public void run() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        Faker faker = new Faker();
        for (int i = 0; i < this.numOfTransp; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner hotelStr = new StringJoiner("|", "", "\n");
            String transporterID = String.valueOf(this.aggregator.genTranspId());
            hotelStr.add(transporterID);
            hotelStr.add(AddQuotMarks.addQuotMarks(faker.company().name().replaceAll("'", "''")));
            String country = RandomCountry.randomCountry().toString();
            hotelStr.add(AddQuotMarks.addQuotMarks(country));
            hotelStr.add(AddQuotMarks.addQuotMarks(this.transpType[random.nextInt(this.transpType.length)]));
            this.queue.put("Transp", hotelStr.toString());
            this.aggregator.addTransporter(transporterID, country);
        }
        return;
    }
}