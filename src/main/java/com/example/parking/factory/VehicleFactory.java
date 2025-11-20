package com.example.parking.factory;

import com.example.parking.model.*;

public class VehicleFactory {
    public static Vehicle createVehicle(String type) {
        if (type == null) return new Car();
        switch (type.toUpperCase()) {
            case "MOTORCYCLE": return new Motorcycle();
            case "BUS": return new Bus();
            default: return new Car();
        }
    }
}
