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
public class HotelGenerator implements Runnable {
    private int numOfHotels;
    private Aggregator aggregator;
    private WriterQueue queue;
    @Override
    public void run() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        Faker faker = new Faker();
        for (int i = 0; i < this.numOfHotels; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner hotelStr = new StringJoiner("|", "", "\n");
            String hotelID = String.valueOf(this.aggregator.genHotelId());
            hotelStr.add(hotelID);
            hotelStr.add(AddQuotMarks.addQuotMarks(faker.company().name().replaceAll("'", "''")));
            hotelStr.add(String.valueOf(random.nextInt(1, 6)));
            String country = RandomCountry.randomCountry();
            String city = RandomCountry.randomCity(country);
            hotelStr.add(AddQuotMarks.addQuotMarks(country));
            hotelStr.add(AddQuotMarks.addQuotMarks(city));
            hotelStr.add(AddQuotMarks.addQuotMarks(faker.address().streetName() + " " + faker.address().buildingNumber()));
            this.queue.put("Hotel", hotelStr.toString());
            this.aggregator.addHotel(hotelID, city);
        }
        return;
    }
}