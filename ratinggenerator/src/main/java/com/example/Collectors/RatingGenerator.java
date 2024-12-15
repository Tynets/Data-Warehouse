package com.example.Collectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.example.ColumnNumbers;
import com.example.Agregators.Agregator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RatingGenerator {
    private final Agregator<String, String[]> agregator;
    private long generatedRatings = 0;
    private long parsedLines = 0;

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
    public long generate(String entitiesFile) {
        try (Scanner scanner = new Scanner(new File(entitiesFile))) {
            scanner.useDelimiter("\n");
            for (int i = 0; i < parsedLines; i++) scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                //if (ThreadLocalRandom.current().nextFloat() > 0.95f) continue;
                String[] tourist = line.split("\\|");
                if (!tourist[ColumnNumbers.statusTourist.get()].equals("Finished")) {
                    parsedLines++;
                    continue;
                }
                String[] tour = agregator.getTour(tourist[ColumnNumbers.tourTourist.get()]);
                if (!tour[ColumnNumbers.statusTour.get()].equals("Finished")) {
                    parsedLines++;
                    continue;
                }
                String[] hotel = agregator.getHotel(tour[ColumnNumbers.hotelTour.get()]);
                String client = tourist[ColumnNumbers.clientTourist.get()];
                String[] rating = new String[7];
                // Tour ID
                rating[0] = tour[ColumnNumbers.IDTour.get()];
                // Client ID
                rating[1] = client;
                // Hotel rating
                float hRating = Float.parseFloat(hotel[ColumnNumbers.ratingHotel.get()]);
                hRating += deviation(-1, 1);
                hRating = clipValue(hRating);
                rating[2] = format(hRating);
                // Beach rating
                float bRating = clipValue(hRating + deviation(-1, 1));
                rating[3] = format(bRating);
                // Transportation rating
                float tRating = ThreadLocalRandom.current().nextFloat(5);
                rating[4] = format(tRating);
                // Overall
                float overall = (hRating + bRating + tRating) / 3;
                overall = clipValue(overall + deviation(-0.5f, 0.5f));
                rating[5] = format(overall);
                // Would you like to recommend
                int rec = overall >= 2.5 ? 1 : 0;
                if (ThreadLocalRandom.current().nextFloat() > 0.9f) rec = (rec + 1) % 2;
                rating[6] = String.valueOf(rec);
                agregator.put(rating);
                generatedRatings++;
                parsedLines++;
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        return generatedRatings;
    }
}