package com.example.parking;

import com.example.parking.model.*;
import com.example.parking.observer.*;
import com.example.parking.strategy.*;
import com.example.parking.factory.VehicleFactory;
import com.example.parking.payment.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        // Setup a sample parking lot
        ParkingLot parkingLot = ParkingLot.Builder.newBuilder()
                .name("CityCenter")
                .numFloors(3)
                .spotsPerFloor(10)
                .build();

        // Add display board observer
        DisplayBoard entryBoard = new DisplayBoard("EntryBoard");
        DisplayBoard exitBoard = new DisplayBoard("ExitBoard");
        parkingLot.registerObserver(entryBoard);
        parkingLot.registerObserver(exitBoard);

        Scanner scanner = new Scanner(System.in);
        ExecutorService exec = Executors.newCachedThreadPool();

        logger.info("Smart Parking System started.");
        boolean running = true;
        while (running) {
            System.out.println("\n=== Smart Parking Menu ===");
            System.out.println("1. Vehicle Entry (Create Ticket)");
            System.out.println("2. Vehicle Exit (Pay & Leave)");
            System.out.println("3. Show Availability");
            System.out.println("4. Admin: Toggle Spot Maintenance");
            System.out.println("5. Admin: Add Entrance/Exit");
            System.out.println("6. Seed Sample Vehicles");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1": {
                        System.out.print("Vehicle type (MOTORCYCLE,CAR,BUS): ");
                        String vtype = scanner.nextLine().trim();
                        Vehicle v = VehicleFactory.createVehicle(vtype);
                        System.out.print("License Plate: ");
                        v.setLicensePlate(scanner.nextLine().trim());
                        // Use strategy selection
                        System.out.print("Allocation strategy (1 Nearest, 2 Farthest): ");
                        String s = scanner.nextLine().trim();
                        ParkingStrategy strategy = s.equals("2") ? new FarthestFirstStrategy() : new NearestFirstStrategy();
                        ParkingTicket ticket = parkingLot.assignSpot(v, strategy);
                        if (ticket != null) {
                            System.out.println("Ticket created: " + ticket.getTicketId());
                            logger.info("Ticket created for " + v);
                        } else {
                            System.out.println("No spot available for vehicle size.");
                            logger.info("No spot available for " + v);
                        }
                        break;
                    }
                    case "2": {
                        System.out.print("Enter Ticket ID: ");
                        String tid = scanner.nextLine().trim();
                        ParkingTicket ticket = parkingLot.getTicket(tid);
                        if (ticket == null) {
                            System.out.println("Ticket not found");
                            break;
                        }
                        // compute fee
                        long minutes = Duration.between(ticket.getEntryTime(), Instant.now()).toMinutes();
                        double fee = FeeCalculator.calculateFee(ticket.getVehicle(), minutes);
                        System.out.println("Parking duration (mins): " + minutes + ", Fee: " + fee);
                        System.out.print("Choose payment method (1 Cash,2 Card,3 UPI): ");
                        String pm = scanner.nextLine().trim();
                        PaymentStrategy payment;
                        if (pm.equals("2")) payment = new CardPayment();
                        else if (pm.equals("3")) payment = new UpiPayment();
                        else payment = new CashPayment();
                        boolean paid = payment.pay(fee);
                        if (paid) {
                            parkingLot.freeSpot(ticket);
                            System.out.println("Payment successful. Vehicle exited.");
                            logger.info("Vehicle with ticket " + ticket.getTicketId() + " exited. Fee=" + fee);
                        } else {
                            System.out.println("Payment failed. Try again.");
                        }
                        break;
                    }
                    case "3": {
                        parkingLot.printAvailability();
                        break;
                    }
                    case "4": {
                        System.out.print("Enter Floor number: ");
                        int f = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Spot id (e.g. F1-S3): ");
                        String sid = scanner.nextLine().trim();
                        System.out.print("Set under maintenance? (true/false): ");
                        boolean um = Boolean.parseBoolean(scanner.nextLine().trim());
                        parkingLot.setSpotMaintenance(sid, um);
                        break;
                    }
                    case "5": {
                        System.out.print("Add Entrance (E) or Exit (X)? ");
                        String ex = scanner.nextLine().trim().toUpperCase();
                        if (ex.equals("E")) parkingLot.addEntrance("Entrance-" + UUID.randomUUID().toString().substring(0,4));
                        else parkingLot.addExit("Exit-" + UUID.randomUUID().toString().substring(0,4));
                        break;
                    }
                    case "6": {
                        // seed sample vehicles entries concurrently
                        for (int i=0;i<4;i++) {
                            final int idx = i;
                            exec.submit(() -> {
                                Vehicle v = VehicleFactory.createVehicle(idx % 3 == 0 ? "CAR" : (idx%3==1?"MOTORCYCLE":"BUS"));
                                v.setLicensePlate("SAMPLE-" + UUID.randomUUID().toString().substring(0,6));
                                parkingLot.assignSpot(v, new NearestFirstStrategy());
                            });
                        }
                        System.out.println("Seeded sample vehicle entries asynchronously.");
                        break;
                    }
                    case "0": {
                        running = false;
                        break;
                    }
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                logger.severe("Error in menu operation: " + e.getMessage());
                //logger.error("Error in menu operation", e);
                System.out.println("Error: " + e.getMessage());
            }
        }
        exec.shutdownNow();
        scanner.close();
        logger.info("Smart Parking System stopped.");
    }
}
