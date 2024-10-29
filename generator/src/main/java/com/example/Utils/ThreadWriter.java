package com.example.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import com.example.Containers.WriterQueue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ThreadWriter implements Runnable {
    private WriterQueue queue;
    private String category;
    private String filename;
    @Override
    public void run() {
        String dirName = "Data " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Calendar.getInstance().getTime());
        File dir = new File(dirName);
        if (!dir.exists()) dir.mkdirs();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(dirName + "/" + this.filename + ".bulk"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> values;
        while(!Thread.currentThread().isInterrupted()) {
            values = this.queue.remove(this.category);
            if (values == null) continue;
            for (String value : values) {
                try {
                    writer.write(value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        values = this.queue.remove(this.category);
        if (values != null) {
            for (String value : values) {
                try {
                    writer.write(value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace(); 
        }
        return;
    }
    
    
}
