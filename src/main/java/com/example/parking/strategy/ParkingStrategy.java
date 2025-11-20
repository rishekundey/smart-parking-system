package com.example.parking.strategy;

import com.example.parking.model.Floor;
import com.example.parking.model.ParkingSpot;
import com.example.parking.model.Vehicle;

import java.util.List;

public interface ParkingStrategy {
    // returns ordered floor numbers to try
    java.util.List<Integer> orderFloors(java.util.List<Integer> floors);
    ParkingSpot selectSpot(Floor floor, Vehicle vehicle);
}
