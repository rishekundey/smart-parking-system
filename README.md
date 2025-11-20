# Smart Parking System (Java)

This repository contains a sample **Smart Parking Lot** backend system implemented in Java 8.
It demonstrates OOP, SOLID principles, Observer/Strategy/Factory design patterns, logging (log4j),
concurrency-safe collections, and a menu-driven CLI for testing.

## Features
- Parking spot allocation based on vehicle size.
- Check-in / Check-out with ParkingTicket.
- Fee calculation by duration and vehicle type.
- Real-time availability updates via Observer (DisplayBoard).
- Admin actions: toggle maintenance, add entrances/exits.
- Parking allocation strategies: Nearest-first, Farthest-first.
- Payment strategies: Cash, Card, UPI.
- Sample data seeding option from CLI.

## Project Structure
- `src/main/java/com/example/parking` - Java sources
- `src/main/resources/log4j.properties` - log4j configuration
- `build.gradle` - Gradle build file (Java 1.8 compatibility)
- `README.md` - this file
- `class-diagram.png` - generated class diagram image

## How to build and run
(Requires Java 8 and Gradle)
```bash
./gradlew build
java -jar build/libs/smart-parking-system-1.0-SNAPSHOT.jar
```

Or run from your IDE by running `com.example.parking.App`.

## Design Patterns used
- Observer -> DisplayBoard listens to ParkingLot updates.
- Strategy -> ParkingStrategy for allocation; PaymentStrategy for payments.
- Factory -> VehicleFactory to create vehicle instances.

## Database Schema (sample - MySQL)
```sql
CREATE TABLE floors (id INT PRIMARY KEY, name VARCHAR(64));
CREATE TABLE parking_spots (id VARCHAR(32) PRIMARY KEY, floor_id INT, size VARCHAR(16), occupied BOOLEAN, maintenance BOOLEAN);
CREATE TABLE vehicles (license_plate VARCHAR(32) PRIMARY KEY, type VARCHAR(16));
CREATE TABLE tickets (ticket_id VARCHAR(32) PRIMARY KEY, license_plate VARCHAR(32), spot_id VARCHAR(32), entry_time DATETIME, exit_time DATETIME, fee DOUBLE);
```

## Class Diagram
See `/class-diagram.png`.

## Notes
- This project focuses on low-level design; persistence layer (DB) is not implemented but schema provided.
- Concurrency is handled with concurrent collections and synchronized methods for critical sections.

