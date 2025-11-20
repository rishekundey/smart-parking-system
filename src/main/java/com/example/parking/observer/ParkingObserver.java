package com.example.parking.observer;

import com.example.parking.model.ParkingLot;

public interface ParkingObserver {
    void update(ParkingLot lot);
}
