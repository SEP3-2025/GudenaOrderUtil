package com.gudena.payment;

import java.time.YearMonth;

public class CardValidator {

    public boolean validateCard(String cardNumber, String firstName, String lastName, String cvv, YearMonth expiry) {
        return validateCardNumber(cardNumber) && validateCVV(cvv) && validateExpiry(expiry);
    }

    private boolean validateCardNumber(String cardNumber) {
        return cardNumber != null && cardNumber.matches("\\d{16}");
    }

    private boolean validateCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    private boolean validateExpiry(YearMonth expiry) {
        YearMonth now = YearMonth.now();
        return expiry.isAfter(now) || expiry.equals(now);
    }
}
