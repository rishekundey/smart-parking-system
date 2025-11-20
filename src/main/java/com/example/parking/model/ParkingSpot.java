package com.example.parking.model;

public class ParkingSpot {
    private final String spotId;
    private final int floor;
    private final SpotSize size;
    private volatile boolean occupied = false;
    private volatile boolean underMaintenance = false;

    public ParkingSpot(String id, int floor, SpotSize size) {
        this.spotId = id;
        this.floor = floor;
        this.size = size;
    }

    public synchronized boolean isAvailable() {
        return !occupied && !underMaintenance;
    }

    public synchronized void occupy() {
        if (isAvailable()) occupied = true;
        else throw new IllegalStateException("Spot not available: " + spotId);
    }

    public synchronized void free() {
        occupied = false;
    }

    public String getSpotId() { return spotId; }
    public SpotSize getSize() { return size; }
    public int getFloor() { return floor; }
    public void setUnderMaintenance(boolean m) { this.underMaintenance = m; }
    public boolean isUnderMaintenance() { return underMaintenance; }
}
