package com.example.parking.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Floor {
    private final int floorNumber;
    private final Map<String, ParkingSpot> spots = new ConcurrentHashMap<>();

    public Floor(int floorNumber, int spotsCount) {
        this.floorNumber = floorNumber;
        // create simple distribution: first spots for motorcycles, then cars, then large
        for (int i=1;i<=spotsCount;i++) {
            SpotSize size = (i % 7 == 0) ? SpotSize.LARGE : ((i%3==0)? SpotSize.COMPACT : SpotSize.MINI);
            String id = "F"+floorNumber+"-S"+i;
            spots.put(id, new ParkingSpot(id, floorNumber, size));
        }
    }

    public Collection<ParkingSpot> getAvailableSpots() {
        return spots.values().stream().filter(ParkingSpot::isAvailable).collect(Collectors.toList());
    }

    public ParkingSpot getSpotById(String id) {
        return spots.get(id);
    }

    public void printStatus() {
        long total = spots.size();
        long avail = spots.values().stream().filter(ParkingSpot::isAvailable).count();
        System.out.println(" Floor " + floorNumber + ": " + avail + "/" + total + " available");
    }

    public int getFloorNumber() { return floorNumber; }

    public Collection<ParkingSpot> getSpots() { return Collections.unmodifiableCollection(spots.values()); }
}
