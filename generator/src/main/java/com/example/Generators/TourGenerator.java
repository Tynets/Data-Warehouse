package com.example.Generators;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.time.DateUtils;
import com.example.Containers.Aggregator;
import com.example.Containers.WriterQueue;
import com.example.Utils.AddQuotMarks;
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
    private final String[] tourStatus = {"New", "Confirmed", "Recruitment", "In progress", "Finished", "Canceled"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private double generateRoomResevation(int numOfTourists, Date startDate, int duration, String city, String tourID) {  
        final ThreadLocalRandom random =  ThreadLocalRandom.current();
        int roomDistribution[] = new int[numOfTourists];
        int sum = 0;
        int rdSize = 0;
        while(true) {
            roomDistribution[rdSize] = random.nextInt(1, 5);
            sum += roomDistribution[rdSize];
            rdSize++;
            if (sum >= numOfTourists) break;
        }
        int difference = sum - numOfTourists;
        for (int i = 0; i < rdSize; i++) {
            if (roomDistribution[i] > difference) {
                roomDistribution[i] -= difference;
                break;
            }
        }
        List<String> tourists = this.aggregator.randomClients(numOfTourists);
        int start = 0, end = 0;
        double cumulativePrice = 0;
        for (int i = 0; i < rdSize; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner reservationStr = new StringJoiner("|", "", "\n");
            String reservationID = String.valueOf(this.aggregator.genReservId());
            reservationStr.add(reservationID);
            reservationStr.add(String.valueOf(random.nextInt(1, 501)));
            reservationStr.add(String.valueOf(roomDistribution[i]));
            reservationStr.add(this.dateFormat.format(DateUtils.addDays(startDate, 1)));
            reservationStr.add(this.dateFormat.format(DateUtils.addDays(startDate, duration - 1)));
            double price = random.nextDouble(2000, 5001);
            cumulativePrice += price;
            reservationStr.add(String.format(Locale.US, "%.2f", price));
            reservationStr.add(this.aggregator.randomHotel(city));
            this.queue.put("Reserv", reservationStr.toString());
            end += roomDistribution[i];
            start = end - roomDistribution[i];
            this.generateTourist(tourID, roomDistribution[i], tourists.subList(start, end), reservationID); 
        }
        return cumulativePrice;
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
            transpStr.add(String.valueOf(this.aggregator.genTransoId()));
            transpStr.add(this.dateFormat.format(dates[i][0]));
            transpStr.add(this.dateFormat.format(dates[i][1]));
            double price = random.nextDouble(500, 1001);
            cumulativePrice += price;
            transpStr.add(String.format(Locale.US, "%.2f", price));
            transpStr.add(this.aggregator.randomTransporter(country));
            transpStr.add(tourID);
            this.queue.put("Transo", transpStr.toString());
        }
        return cumulativePrice;
    }
    private void generateTourist(String tourID, int numOfTourists, List<String> tourists, String reservationID) {
        for (int i = 0; i < numOfTourists; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner touristStr = new StringJoiner("|", "", "\n");
            touristStr.add(tourID);
            touristStr.add(tourists.get(i));
            touristStr.add(AddQuotMarks.addQuotMarks("Finished"));
            touristStr.add(reservationID);
            this.queue.put("Tourist", touristStr.toString());
        }
        return;
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
            int duration = random.nextInt(7, 15);
            tourStr.add(AddQuotMarks.addQuotMarks(String.format("%s, %s %d days tour", city, country, duration)));
            Date startDate = RandomDate.randomDate(this.startYear, this.endYear);
            Date endDate = DateUtils.addDays(startDate, duration);
            tourStr.add(this.dateFormat.format(startDate));
            tourStr.add(this.dateFormat.format(endDate));
            tourStr.add(AddQuotMarks.addQuotMarks(country));
            tourStr.add(AddQuotMarks.addQuotMarks(city));
            int maxNumOfPart = random.nextInt(5, 11);
            double price = this.generateRoomResevation(Math.random() > 0.1 ? maxNumOfPart : maxNumOfPart - 1, startDate, duration, city, tourID);
            price += this.generateTransportation(startDate, duration, country, tourID);
            tourStr.add(String.format(Locale.US, "%.2f", (price * 10) / 9));
            int statusIdx = Math.random() < 0.003 ? this.tourStatus.length - 1 : this.tourStatus.length - 2;
            tourStr.add(AddQuotMarks.addQuotMarks(this.tourStatus[statusIdx]));
            tourStr.add(String.valueOf(maxNumOfPart));
            tourStr.add(this.aggregator.randomHotel(city));
            this.queue.put("Tour", tourStr.toString());
        }
        return;
    }
}
