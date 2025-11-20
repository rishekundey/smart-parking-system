package com.example.parking.payment;

import java.util.logging.Logger;

public class CardPayment implements PaymentStrategy {
    private static final Logger logger = Logger.getLogger(CardPayment.class.getName());
    @Override
    public boolean pay(double amount) {
        // simulate card processing
        logger.info("Processing card payment: " + amount);
        return true;
    }
}
