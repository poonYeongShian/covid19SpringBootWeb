package com.example.servingwebcontent.models.apimodel;

public class Address {

    private double latitude;
    private double longitude;
    private String unitNumber;
    private String street;
    private String suburb;
    private String state;
    private String postcode;

    public Address(double latitude, double longitude, String unitNumber, String street, String suburb, String state,
            String postcode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.unitNumber = unitNumber;
        this.street = street;
        this.suburb = suburb;
        this.state = state;
        this.postcode = postcode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String toJSONStringFormat() {
        return "Address:{" +
                "\"latitude\":" + latitude +
                ", \"longitude\":" + longitude +
                ", \"unitNumber\":\"" + unitNumber + '\"' +
                ", \"street\":\"" + street + '\"' +
                ", \"suburb\":\"" + suburb + '\"' +
                ", \"street2\":\"" + "-" + '\"' +
                ", \"state\":\"" + state + '\"' +
                ", \"postcode\":\"" + postcode + '\"' +
                ", \"additionalInfo\":" + "{}" +
                '}';
    }

    @Override
    public String toString() {
        return "Address{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", unitNumber='" + unitNumber + '\'' +
                ", street='" + street + '\'' +
                ", suburb='" + suburb + '\'' +
                ", state='" + state + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
