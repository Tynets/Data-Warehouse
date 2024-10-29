package com.example.Generators;

import java.text.SimpleDateFormat;
import java.util.StringJoiner;
import com.example.Containers.Aggregator;
import com.example.Containers.WriterQueue;
import com.example.Utils.AddQuotMarks;
import com.example.Utils.RandomDate;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientGenerator implements Runnable {
    private int numOfClient;
    private Aggregator aggregator;
    private WriterQueue queue;
    @Override
    public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Faker faker = new Faker();
        for (int i = 0; i < this.numOfClient; i++) {
            if (Thread.currentThread().isInterrupted()) break;
            StringJoiner clientStr = new StringJoiner("|", "", "\n");
            String clientID = String.valueOf(this.aggregator.genClientId());
            clientStr.add(clientID);
            clientStr.add(AddQuotMarks.addQuotMarks(faker.name().firstName()));
            clientStr.add(AddQuotMarks.addQuotMarks(faker.name().lastName()));
            clientStr.add(dateFormat.format(RandomDate.randomDate(1950, 2010)));
            clientStr.add(AddQuotMarks.addQuotMarks(faker.numerify("#########")));
            this.queue.put("Client", clientStr.toString());
            this.aggregator.addClient(clientID);
        }
        return;
    }
}
