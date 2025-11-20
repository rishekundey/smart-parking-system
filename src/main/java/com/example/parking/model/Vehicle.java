package com.example.parking.model;

public abstract class Vehicle {
    private String licensePlate;
    public abstract VehicleSize getSize();
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + licensePlate + ")";
    }
}
