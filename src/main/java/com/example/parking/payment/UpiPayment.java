package com.example.parking.payment;

import java.util.logging.Logger;

public class UpiPayment implements PaymentStrategy {
    private static final Logger logger = Logger.getLogger(UpiPayment.class.getName());
    @Override
    public boolean pay(double amount) {
        logger.info("Processing UPI payment: " + amount);
        return true;
    }
}
