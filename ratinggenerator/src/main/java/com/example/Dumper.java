package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Deque;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Dumper {
    private final Deque<String[]> ratings;
    private final String dumpFile;

    public void dump() {
        try (FileWriter writer = new FileWriter(dumpFile)) {
            for (String[] r : ratings) writer.write(String.join(",", r) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}