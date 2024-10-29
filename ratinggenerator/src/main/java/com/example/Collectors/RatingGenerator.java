package com.example.Collectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.example.ColumnNumbers;
import com.example.Agregators.Agregator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RatingGenerator implements Runnable {
    private final Agregator<String, String[]> agregator;
    private final String entitiesFile;

    private float clipValue(float value) {
        if (value > 5.0) return 5.0f;
        if (value < 0) return 0.0f;
        return value;
    }
    private float deviation(float lower, float higher) {
        return ThreadLocalRandom.current().nextFloat(lower, higher);
    }
    private String format(float value) {
        String ret = String.format(java.util.Locale.US, "%.1f", value);
        return String.valueOf(ret);
    }
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(new File(entitiesFile))) {
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (ThreadLocalRandom.current().nextFloat() > 0.95f) continue;
                String[] tourist = line.split("\\|");
                if (!tourist[ColumnNumbers.statusTourist.get()].equals("'Finished'")) continue;
                String[] tour = agregator.getTour(tourist[ColumnNumbers.tourTourist.get()]);
                if (!tour[ColumnNumbers.statusTour.get()].equals("'Finished'")) continue;
                String[] hotel = agregator.getHotel(tour[ColumnNumbers.hotelTour.get()]);
                String[] rating = new String[6];
                // Tour ID
                rating[0] = tour[0];
                // Hotel rating
                float hRating = Float.parseFloat(hotel[ColumnNumbers.ratingHotel.get()]);
                hRating += deviation(-1, 1);
                hRating = clipValue(hRating);
                rating[1] = format(hRating);
                // Beach rating
                float bRating = clipValue(hRating + deviation(-1, 1));
                rating[2] = format(bRating);
                // Transportation rating
                float tRating = ThreadLocalRandom.current().nextFloat(5);
                rating[3] = format(tRating);
                // Overall
                float overall = (hRating + bRating + tRating) / 3;
                overall = clipValue(overall + deviation(-0.5f, 0.5f));
                rating[4] = format(overall);
                // Would you like to recommend
                int rec = overall >= 2.5 ? 1 : 0;
                if (ThreadLocalRandom.current().nextFloat() > 0.9f) rec = (rec + 1) % 2;
                rating[5] = String.valueOf(rec);
                agregator.put(null, rating);
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }
}