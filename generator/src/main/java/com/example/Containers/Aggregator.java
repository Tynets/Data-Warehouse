package com.example.Containers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

import com.example.Utils.SequentialID;

public class Aggregator implements Serializable {
    // <HotelID, City/CityCountry>
    ConcurrentMap<String, List<String>> hotels;
    // <ClientID, Country>
    ConcurrentMap<String, List<String>> transporters;
    // <Dummy/IDENTITY(1, 1), ClientID>
    ConcurrentMap<Integer, String> clients;
    private int counter = 0;
    private SequentialID hotelIdGen;
    private SequentialID transpIdGen;
    private SequentialID clientIdGen;
    private SequentialID tourIdGen;
    private SequentialID reservIdGen;
    private SequentialID transoIdGen;
    public Aggregator() {
        this.hotels = new ConcurrentHashMap<String, List<String>>();
        this.transporters = new ConcurrentHashMap<String, List<String>>();
        this.clients = new ConcurrentHashMap<Integer, String>();
        this.hotelIdGen = new SequentialID();
        this.transpIdGen = new SequentialID();
        this.clientIdGen = new SequentialID();
        this.tourIdGen = new SequentialID();
        this.reservIdGen = new SequentialID();
        this.transoIdGen = new SequentialID();
        return;
    }
    public void addHotel(String hotelID, String city) {
        synchronized (this.hotels) {
            List<String> value = this.hotels.get(city);
            if (value == null) value = new LinkedList<String>();
            value.add(hotelID);
            this.hotels.put(city, value);
        }
        return;
    }
    public void addTransporter(String transporterID, String country) {
        synchronized (this.transporters) {
            List<String> value = this.transporters.get(country);
            if (value == null) value = new LinkedList<String>();
            value.add(transporterID);
            this.transporters.put(country, value);
        }
        return;
    }
    public void addClient(String clientID) {
        this.clients.putIfAbsent(counter, clientID);
        counter++;
        return;
    }
    public String randomHotel(String city) {
        List<String> grouped = this.hotels.get(city);
        return grouped.get(ThreadLocalRandom.current().nextInt(grouped.size()));
    }
    public String randomTransporter(String country) {
        List<String> grouped = this.transporters.get(country);
        return grouped.get(ThreadLocalRandom.current().nextInt(grouped.size()));
    }
    public String randomClient() {
        return this.clients.get(ThreadLocalRandom.current().nextInt(counter - 1));
    }
    public List<String> randomClients(int n) {
        int idx = ThreadLocalRandom.current().nextInt(counter - n + 1);
        List<String> rClients = new LinkedList<String>();
        for (int i = idx; i < idx + n; i++) {
            rClients.add(this.clients.get(i));
        }
        return rClients;
    }
    public long genHotelId() {
        return this.hotelIdGen.getId();
    }
    public long genTranspId() {
        return this.transpIdGen.getId();
    }
    public long genClientId() {
        return this.clientIdGen.getId();
    }
    public long genTourId() {
        return this.tourIdGen.getId();
    }
    public long genReservId() {
        return this.reservIdGen.getId();
    }
	public long genTransoId() {
		return this.transoIdGen.getId();
	}
    public void serialize() {
        try {
            FileOutputStream file = new FileOutputStream("aggregator.bin");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(this.hotels);
            output.writeObject(this.transporters);
            output.writeObject(this.clients);
            output.writeObject(this.counter);
            output.writeObject(this.hotelIdGen);
            output.writeObject(this.transpIdGen);
            output.writeObject(this.clientIdGen);
            output.writeObject(this.tourIdGen);
            output.writeObject(this.reservIdGen);
            output.writeObject(this.transoIdGen);
            output.close();
            file.close();
        } catch(IOException e) {
            System.out.println(e);
        }
        return;
    }
    @SuppressWarnings("unchecked")
    public void deserialize() {
        try {
            FileInputStream file = new FileInputStream ("aggregator.bin");
            ObjectInputStream input = new ObjectInputStream(file);
            this.hotels = (ConcurrentMap<String, List<String>>) input.readObject();
            this.transporters = (ConcurrentMap<String, List<String>>) input.readObject();
            this.clients = (ConcurrentMap<Integer, String>) input.readObject();
            this.counter = (int) input.readObject();
            this.hotelIdGen = (SequentialID) input.readObject();
            this.transpIdGen = (SequentialID) input.readObject();
            this.clientIdGen = (SequentialID) input.readObject();
            this.tourIdGen = (SequentialID) input.readObject();
            this.reservIdGen = (SequentialID) input.readObject();
            this.transoIdGen = (SequentialID) input.readObject();
            input.close();
            file.close();
        } catch(IOException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return;
    }
}
