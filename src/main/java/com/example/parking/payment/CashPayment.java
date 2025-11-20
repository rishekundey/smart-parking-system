package com.example.parking.payment;

import java.util.logging.Logger;

public class CashPayment implements PaymentStrategy {
    private static final Logger logger = Logger.getLogger(CashPayment.class.getName());
    @Override
    public boolean pay(double amount) {
        // simulate cash accepted
        logger.info("Cash payment of " + amount + " accepted");
        return true;
    }
}
