package com.gudena.shipping;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationsShippingsDto {
    @JsonProperty("OriginCountry")
    private String originCountry;
    @JsonProperty("OriginPostalCode")
    private String originPostalCode;
    @JsonProperty("DestinationCountry")
    private String destinationCountry;
    @JsonProperty("DestinationPostalCode")
    private String destinationPostalCode;

    // Getters and setters
    public String getOriginCountry() { return originCountry; }
    public void setOriginCountry(String originCountry) { this.originCountry = originCountry; }

    public String getOriginPostalCode() { return originPostalCode; }
    public void setOriginPostalCode(String originPostalCode) { this.originPostalCode = originPostalCode; }

    public String getDestinationCountry() { return destinationCountry; }
    public void setDestinationCountry(String destinationCountry) { this.destinationCountry = destinationCountry; }

    public String getDestinationPostalCode() { return destinationPostalCode; }
    public void setDestinationPostalCode(String destinationPostalCode) { this.destinationPostalCode = destinationPostalCode; }
}
