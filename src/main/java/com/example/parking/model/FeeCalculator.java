package com.example.parking.model;

public class FeeCalculator {
    // basic fee logic: base per hour per vehicle type
    public static double calculateFee(Vehicle v, long minutes) {
        double hours = Math.ceil(minutes / 60.0);
        switch (v.getSize()) {
            case MOTORCYCLE: return 10 + hours * 10; // base + per hour
            case CAR: return 20 + hours * 30;
            case BUS: return 50 + hours * 100;
        }
        return 0;
    }
}
