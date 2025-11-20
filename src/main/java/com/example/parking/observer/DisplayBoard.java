package com.example.parking.observer;

import com.example.parking.model.Floor;
import com.example.parking.model.ParkingLot;

import java.util.Map;

public class DisplayBoard implements ParkingObserver {
    private final String name;
    public DisplayBoard(String name) { this.name = name; }
    @Override
    public void update(ParkingLot lot) {
        System.out.println("[" + name + "] Updating display... ");
        for (Map.Entry<Integer, Floor> e : lot.getFloors().entrySet()) {
            System.out.println("  Floor " + e.getKey() + ": " + e.getValue().getAvailableSpots().size() + " available");
        }
    }
}
