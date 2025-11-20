package com.example.parking.model;

import com.example.parking.observer.ParkingObserver;
import com.example.parking.strategy.ParkingStrategy;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class ParkingLot {
    private static final Logger logger = Logger.getLogger(ParkingLot.class.getName());

    private final String name;
    private final Map<Integer, Floor> floors = new ConcurrentHashMap<>();
    private final Map<String, ParkingTicket> activeTickets = new ConcurrentHashMap<>();
    private final List<ParkingObserver> observers = new ArrayList<>();
    private final AtomicInteger ticketCounter = new AtomicInteger(1);
    private final AtomicInteger entranceCounter = new AtomicInteger(0);
    private final AtomicInteger exitCounter = new AtomicInteger(0);

    private ParkingLot(String name) {
        this.name = name;
    }

    public static class Builder {
        private String name = "DefaultLot";
        private int numFloors = 1;
        private int spotsPerFloor = 10;

        public static Builder newBuilder() { return new Builder(); }
        public Builder name(String n) { this.name = n; return this; }
        public Builder numFloors(int f) { this.numFloors = f; return this; }
        public Builder spotsPerFloor(int s) { this.spotsPerFloor = s; return this; }
        public ParkingLot build() {
            ParkingLot lot = new ParkingLot(name);
            for (int i=1;i<=numFloors;i++) {
                lot.floors.put(i, new Floor(i, spotsPerFloor));
            }
            return lot;
        }
    }

    public synchronized ParkingTicket assignSpot(Vehicle vehicle, ParkingStrategy strategy) {
        // select floor and spot using provided strategy
        for (int floorNum : strategy.orderFloors(new ArrayList<>(floors.keySet()))) {
            Floor floor = floors.get(floorNum);
            ParkingSpot spot = strategy.selectSpot(floor, vehicle);
            if (spot != null) {
                // allocate
                spot.occupy();
                String ticketId = "T" + ticketCounter.getAndIncrement();
                ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot, Instant.now());
                activeTickets.put(ticketId, ticket);
                notifyObservers();
                logger.info("Allocated spot " + spot.getSpotId() + " to " + vehicle);
                return ticket;
            }
        }
        return null;
    }

    public synchronized void freeSpot(ParkingTicket ticket) {
        if (ticket == null) return;
        ParkingSpot spot = ticket.getSpot();
        spot.free();
        ticket.setExitTime(Instant.now());
        activeTickets.remove(ticket.getTicketId());
        notifyObservers();
        logger.info("Freed spot " + spot.getSpotId() + " for ticket " + ticket.getTicketId());
    }

    public ParkingTicket getTicket(String ticketId) {
        return activeTickets.get(ticketId);
    }

    public void printAvailability() {
        System.out.println("Availability for Parking Lot: " + name);
        floors.values().forEach(Floor::printStatus);
    }

    public void registerObserver(ParkingObserver obs) {
        observers.add(obs);
        obs.update(this);
    }

    public void notifyObservers() {
        for (ParkingObserver obs : observers) obs.update(this);
    }

    public Map<Integer, Floor> getFloors() { return Collections.unmodifiableMap(floors); }

    public void setSpotMaintenance(String spotId, boolean underMaintenance) {
        for (Floor f : floors.values()) {
            ParkingSpot s = f.getSpotById(spotId);
            if (s != null) {
                s.setUnderMaintenance(underMaintenance);
                notifyObservers();
                logger.info("Spot " + spotId + " maintenance set to " + underMaintenance);
                return;
            }
        }
        logger.warning("Spot id not found: " + spotId);
    }

    public void addEntrance(String name) {
        int id = entranceCounter.incrementAndGet();
        logger.info("Added entrance: " + name + "-" + id);
    }

    public void addExit(String name) {
        int id = exitCounter.incrementAndGet();
        logger.info("Added exit: " + name + "-" + id);
    }
}
