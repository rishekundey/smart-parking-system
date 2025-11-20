package com.example.parking.model;

import java.time.Instant;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final Instant entryTime;
    private Instant exitTime;

    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSpot spot, Instant entryTime) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = entryTime;
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public Instant getEntryTime() { return entryTime; }
    public Instant getExitTime() { return exitTime; }
    public void setExitTime(Instant t) { this.exitTime = t; }
}
