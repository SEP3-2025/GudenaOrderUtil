package com.gudena.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCardDto {
    @JsonProperty("CardNumber")
    private String cardNumber;

    @JsonProperty("CvvCode")
    private String cvvCode;

    @JsonProperty("ExpiryDate")
    private String expiryDate;

    @JsonProperty("HolderFullName")
    private String holderFullName;

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCvvCode() { return cvvCode; }
    public void setCvvCode(String cvvCode) { this.cvvCode = cvvCode; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getHolderFullName() { return holderFullName; }
    public void setHolderFullName(String holderFullName) { this.holderFullName = holderFullName; }
}
