package com.example.Generators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.example.Containers.Aggregator;
import com.example.Containers.WriterQueue;
import com.example.Utils.RandomCountry;
import com.example.Utils.RandomDate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TourGenerator implements Runnable {
    private int numOfTours;
    private int startYear;
    private int endYear;
    private Aggregator aggregator;
    private WriterQueue queue;
    private final String[] tourStatus = {"New", "Confirmed", "Recruitment", "In progress", "Finished", "Cancelled"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Pair<Double, Integer> generateRoomResevation(Date startDate, int duration, String country, String city, String tourID, String hotelID, int statusIdx) {  
        final ThreadLocalRandom random =  ThreadLocalRandom.current();
        int roomDistribution[] = IntStream.generate(() -> random.nextInt(1, 4)).limit(4).toArray();
        int numOfTourists = IntStream.of(roomDistribution).sum();
        List<String> tourists = this.aggregator.randomClients(numOfTourists);
        int start = 0, end = 0;
        double cumulativePrice = 0;
        for (int i = 0; i < roomDistribution.length; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner reservationStr = new StringJoiner("|", "", "\n");
            String reservationID = String.valueOf(this.aggregator.genReservId());
            int priceBounds[] = RandomCountry.hotelPriceBounds(country, city);
            double price = random.nextDouble(priceBounds[0], priceBounds[1] + 1);
            cumulativePrice += price;
            reservationStr.add(reservationID);
            reservationStr.add(String.valueOf(random.nextInt(1, 501)));
            reservationStr.add(String.valueOf(roomDistribution[i]));
            reservationStr.add(this.dateFormat.format(DateUtils.addDays(startDate, 1)));
            reservationStr.add(this.dateFormat.format(DateUtils.addDays(startDate, duration - 1)));
            reservationStr.add(String.format(Locale.US, "%.2f", price));
            reservationStr.add(hotelID);
            this.queue.put("Reserv", reservationStr.toString());
            end += roomDistribution[i];
            start = end - roomDistribution[i];
            this.generateTourist(tourID, roomDistribution[i], tourists.subList(start, end), reservationID, statusIdx); 
        }
        return new ImmutablePair<Double, Integer>(cumulativePrice / roomDistribution.length, numOfTourists);
    }
    private void generateTourist(String tourID, int numOfTourists, List<String> tourists, String reservationID, int statusIdx) {
        for (int i = 0; i < numOfTourists; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner touristStr = new StringJoiner("|", "", "\n");
            touristStr.add(tourID);
            touristStr.add(tourists.get(i));
            touristStr.add(this.tourStatus[statusIdx]);
            touristStr.add(reservationID);
            this.queue.put("Tourist", touristStr.toString());
        }
        return;
    }
    private double generateTransportation(Date startDate, int duration, String country, String tourID) {
        final ThreadLocalRandom random =  ThreadLocalRandom.current();
        Date endDate = DateUtils.addDays(startDate, duration);
        Date startPlusOne = DateUtils.addDays(startDate, 1);
        Date endMinusOne = DateUtils.addDays(startDate, duration - 1);
        Date[][] dates = {{startDate, startDate}, {startDate, startPlusOne},
                        {startPlusOne, startPlusOne}, {endMinusOne, endMinusOne},
                        {endMinusOne, endDate}, {endDate, endDate}};
        double cumulativePrice = 0;
        for (int i = 0; i < 6; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner transpStr = new StringJoiner("|", "", "\n");
            int priceBounds[] = RandomCountry.transportationPriceBounds(country);
            double price = random.nextDouble(priceBounds[0], priceBounds[1] + 1);
            cumulativePrice += price;
            transpStr.add(String.valueOf(this.aggregator.genTransoId()));
            transpStr.add(this.dateFormat.format(dates[i][0]));
            transpStr.add(this.dateFormat.format(dates[i][1]));
            transpStr.add(String.format(Locale.US, "%.2f", price));
            transpStr.add(this.aggregator.randomTransporter(country));
            transpStr.add(tourID);
            this.queue.put("Transo", transpStr.toString());
        }
        return cumulativePrice;
    }
    @Override
    public void run() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < this.numOfTours; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner tourStr = new StringJoiner("|", "", "\n");
            String tourID = String.valueOf(this.aggregator.genTourId());
            tourStr.add(tourID);
            String country = RandomCountry.randomCountry();
            String city = RandomCountry.randomCity(country);
            String hotelID = this.aggregator.randomHotel(city);
            int duration = random.nextInt(7, 15);
            Date startDate = RandomDate.randomDate(this.startYear, this.endYear);
            Date endDate = DateUtils.addDays(startDate, duration);
            int statusIdx = Math.random() < 0.003 ? this.tourStatus.length - 1 : this.tourStatus.length - 2;
            Pair<Double, Integer> priceNumOfTourists = this.generateRoomResevation(startDate, duration, country, city, tourID, hotelID, statusIdx);
            double price = priceNumOfTourists.getLeft() + this.generateTransportation(startDate, duration, country, tourID);
            tourStr.add(String.format("%s, %s %d days tour", city, country, duration));
            tourStr.add(this.dateFormat.format(startDate));
            tourStr.add(this.dateFormat.format(endDate));
            tourStr.add(country);
            tourStr.add(city);
            tourStr.add(String.format(Locale.US, "%.2f", (price * 10) / 9));
            tourStr.add(this.tourStatus[statusIdx]);
            tourStr.add(String.valueOf(Math.random() > 0.1 ? priceNumOfTourists.getRight() : priceNumOfTourists.getRight() + 1));
            tourStr.add(hotelID);
            this.queue.put("Tour", tourStr.toString());
        }
        return;
    }
}