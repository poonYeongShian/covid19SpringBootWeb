package com.example.servingwebcontent.models.apimodel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Booking {
    private String bookingId;
    private String customerId;
    private String customerName;
    private String testingSiteId;
    private String testingSiteName;
    private String smsPin;
    private String startTime;
    private String status;
    private String url;
    private String qr;
    private boolean testingDone;
    private boolean cancelBooking;
    private String modifiedTimestamp;
    private String createdAt;
    private List<CovidTest> covidTests;
    private String recentUpdateTime;
    private List<PastBooking> pastBookings;
    private boolean lapsedBooking;
    private String updateDesc;
    private String previousTestSite;

    public Booking(String bookingId, String customerId, String customerName, String testingSiteId,
            String testingSiteName, String smsPin,
            String startTime, String status, String url, String qr, boolean testingDone, boolean cancelBooking,
            String modifiedTimestamp, String createdAt) {
        this.covidTests = new ArrayList<>();
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.testingSiteId = testingSiteId;
        this.testingSiteName = testingSiteName;
        this.smsPin = smsPin;
        this.startTime = startTime;
        this.status = status;
        this.url = url;
        this.qr = qr;
        this.testingDone = testingDone;
        this.cancelBooking = cancelBooking;
        this.modifiedTimestamp = modifiedTimestamp;
        this.createdAt = createdAt;

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        LocalDateTime timeNow = LocalDateTime.now();

        if (startTime.length() == 23) {
            startTime = startTime + "Z";
        }

        LocalDateTime startTime2 = LocalDateTime.parse(startTime, inputFormatter);
        if (timeNow.isAfter(startTime2)) {
            this.lapsedBooking = true;
        }
    }

    public Booking(String bookingId, String testingSiteId,
            String testingSiteName, String smsPin,
            String startTime, String status) {
        this.covidTests = new ArrayList<>();
        this.bookingId = bookingId;
        this.testingSiteId = testingSiteId;
        this.testingSiteName = testingSiteName;
        this.smsPin = smsPin;
        this.startTime = startTime;
        this.status = status;


        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        LocalDateTime timeNow = LocalDateTime.now();

        if (startTime.length() == 23) {
            startTime = startTime + "Z";
        }

        LocalDateTime startTime2 = LocalDateTime.parse(startTime, inputFormatter);
        if (timeNow.isAfter(startTime2)) {
            this.lapsedBooking = true;
        }
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFormattedStartTime() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy hh:mm a", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(startTime, inputFormatter);
        String formattedDate = outputFormatter.format(date);
        return formattedDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTestingSiteId() {
        return testingSiteId;
    }

    public void setTestingSiteId(String testingSiteId) {
        this.testingSiteId = testingSiteId;
    }

    public String getTestingSiteName() {
        return testingSiteName;
    }

    public void setTestingSiteName(String testingSiteName) {
        this.testingSiteName = testingSiteName;
    }

    public String getSmsPin() {
        return smsPin;
    }

    public void setSmsPin(String smsPin) {
        this.smsPin = smsPin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public boolean getTestingDone() {
        return testingDone;
    }

    public void setTestingDone(boolean testingDone) {
        this.testingDone = testingDone;
    }

    public boolean getCancelBooking() {
        return cancelBooking;
    }

    public void setCancelBooking(boolean cancelBooking) {
        this.cancelBooking = cancelBooking;
    }

    public String getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(String modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedModifiedTimestamp() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy hh:mm a", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(modifiedTimestamp + ":00.000Z", inputFormatter);
        String formattedDate = outputFormatter.format(date);
        return formattedDate;
    }

    public List<CovidTest> getCovidTests() {
        return covidTests;
    }

    public void setCovidTests(List<CovidTest> covidTests) {
        this.covidTests = covidTests;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    public List<PastBooking> getPastBookings() {
        return pastBookings;
    }

    public void setPastBookings(List<PastBooking> pastBookings) {
        this.pastBookings = pastBookings;
    }

    public boolean getLapsedBooking() {
        return lapsedBooking;
    }

    public void setLapsedBooking(boolean lapsedBooking) {
        this.lapsedBooking = lapsedBooking;
    }

    public String getUpdateDesc() {
        return updateDesc;
    }

    public void setUpdateDesc(String updateDesc) {
        this.updateDesc = updateDesc;
    }

    public String getPreviousTestSite() {
        return previousTestSite;
    }

    public void setPreviousTestSite(String previousTestSite) {
        this.previousTestSite = previousTestSite;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", testingSiteId='" + testingSiteId + '\'' +
                ", testingSiteName='" + testingSiteName + '\'' +
                ", smsPin='" + smsPin + '\'' +
                ", startTime='" + startTime + '\'' +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                ", qr='" + qr + '\'' +
                ", testingDone=" + testingDone +
                ", cancelBooking=" + cancelBooking +
                ", modifiedTimestamp='" + modifiedTimestamp + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", covidTests=" + covidTests +
                ", recentUpdateTime='" + recentUpdateTime + '\'' +
                ", pastBookings=" + pastBookings +
                ", lapsedBooking=" + lapsedBooking +
                ", updateDesc='" + updateDesc + '\'' +
                ", previousTestSite='" + previousTestSite + '\'' +
                '}';
    }
}
