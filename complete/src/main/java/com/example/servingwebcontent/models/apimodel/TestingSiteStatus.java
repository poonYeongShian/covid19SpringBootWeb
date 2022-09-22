package com.example.servingwebcontent.models.apimodel;

import java.time.LocalTime;

public class TestingSiteStatus {

    private String typeOfFacility;
    private boolean onSiteBookingAndTesting;
    private int waitingTimeInMins;
    private String openingTime;
    private String closingTime;
    private String openOrClosed;

    public TestingSiteStatus(String typeOfFacility, boolean onSiteBookingAndTesting, int waitingTimeInMins,
            String openingTime, String closingTime, String openOrClosed) {
        this.typeOfFacility = typeOfFacility;
        this.onSiteBookingAndTesting = onSiteBookingAndTesting;
        this.waitingTimeInMins = waitingTimeInMins;
        this.openingTime = openingTime;
        this.closingTime = closingTime;

        LocalTime timeNow = LocalTime.now();

        LocalTime open = LocalTime.parse(openingTime);
        LocalTime close = LocalTime.parse(closingTime);
        int openValue = timeNow.compareTo(open);
        int closeValue = timeNow.compareTo(close);

        // Get local time and change the status from "open" to "closed"
        if (openValue > 0 && closeValue < 0) {
            this.openOrClosed = "Open";
        } else {
            this.openOrClosed = "Closed";
        }
    }

    public String getTypeOfFacility() {
        return typeOfFacility;
    }

    public void setTypeOfFacility(String typeOfFacility) {
        this.typeOfFacility = typeOfFacility;
    }

    public boolean isOnSiteBookingAndTesting() {
        return onSiteBookingAndTesting;
    }

    public void setOnSiteBookingAndTesting(boolean onSiteBookingAndTesting) {
        this.onSiteBookingAndTesting = onSiteBookingAndTesting;
    }

    public int getWaitingTimeInMins() {
        return waitingTimeInMins;
    }

    public void setWaitingTimeInMins(int waitingTimeInMins) {
        this.waitingTimeInMins = waitingTimeInMins;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public String getOpenOrClosed() {
        return openOrClosed;
    }

    public String toJSONStringFormat() {
        return "additionalInfo:{" +
                "\"typeOfFacility\":\"" + typeOfFacility + '\"' +
                ", \"onSiteBookingAndTesting\":" + onSiteBookingAndTesting +
                ", \"waitingTimeInMins\":" + waitingTimeInMins +
                ", \"openingTime\":" + openingTime +
                ", \"closingTime\":" + closingTime +
                ", \"openOrClosed\":" + openOrClosed +
                '}';
    }

    @Override
    public String toString() {
        return "TestingSiteStatus{" +
                "typeOfFacility='" + typeOfFacility + '\'' +
                ", onSiteBookingAndTesting=" + onSiteBookingAndTesting +
                ", waitingTimeInMins=" + waitingTimeInMins +
                ", \"openingTime\":" + openingTime +
                ", \"closingTime\":" + closingTime +
                ", \"openOrClosed\":" + openOrClosed +
                '}';
    }
}
