package com.example.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDate {
    public static Date randomDate(int lowerYear, int upperYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, ThreadLocalRandom.current().nextInt(lowerYear, upperYear));
        calendar.set(Calendar.DAY_OF_YEAR, ThreadLocalRandom.current().nextInt(365) + 1);
        return calendar.getTime();
    }
}
