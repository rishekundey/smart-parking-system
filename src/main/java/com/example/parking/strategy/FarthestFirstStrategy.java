package com.example.parking.strategy;

import com.example.parking.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class FarthestFirstStrategy implements ParkingStrategy {
    @Override
    public List<Integer> orderFloors(List<Integer> floors) {
        floors.sort(Collections.reverseOrder());
        return floors;
    }

    @Override
    public ParkingSpot selectSpot(Floor floor, Vehicle vehicle) {
        // pick last compatible spot id to simulate farthest
        return floor.getSpots().stream()
                .filter(ParkingSpot::isAvailable)
                .filter(s -> isCompatible(s.getSize(), vehicle.getSize()))
                .sorted(Comparator.comparing(ParkingSpot::getSpotId).reversed())
                .findFirst().orElse(null);
    }

    private boolean isCompatible(SpotSize spot, VehicleSize v) {
        if (v == VehicleSize.MOTORCYCLE) return true;
        if (v == VehicleSize.CAR) return spot == SpotSize.COMPACT || spot == SpotSize.LARGE;
        return v == VehicleSize.BUS && spot == SpotSize.LARGE;
    }
}
