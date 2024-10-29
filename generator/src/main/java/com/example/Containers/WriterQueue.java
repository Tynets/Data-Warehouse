package com.example.Containers;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WriterQueue {
    private ConcurrentMap<String, List<String>> queue;
    public WriterQueue() {
        this.queue = new ConcurrentHashMap<String, List<String>>();
        return;
    }
    public synchronized void put(String category, String content) {
        List<String> value = this.queue.get(category);
        if (value == null) value = new LinkedList<String>();
        value.add(content);
        this.queue.put(category, value);
        return;
    }
    public synchronized List<String> remove(String category) {
        return this.queue.remove(category);
    }
}
