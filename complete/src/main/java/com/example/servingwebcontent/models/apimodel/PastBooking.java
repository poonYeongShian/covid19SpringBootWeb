package com.example.servingwebcontent.models.apimodel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PastBooking {
    private String timestamp;
    private String testingSiteId;
    private String testingSiteName;
    private String startTime;

    public PastBooking(String timestamp, String testingSiteId, String testingSiteName, String startTime) {
        this.timestamp = timestamp;
        this.testingSiteId = testingSiteId;
        this.testingSiteName = testingSiteName;
        this.startTime = startTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy hh:mm a", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(timestamp + ":00.000Z", inputFormatter);
        String formattedDate = outputFormatter.format(date);
        return formattedDate;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFormattedStartTime() {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy hh:mm a", Locale.ENGLISH);
        LocalDateTime date = LocalDateTime.parse(startTime, inputFormatter);
        String formattedDate = outputFormatter.format(date);
        return formattedDate;
    }

    @Override
    public String toString() {
        return "PastBooking{" +
                "timestamp='" + timestamp + '\'' +
                ", formattedTimestamp='" + getFormattedTimestamp() + '\'' +
                ", testingSiteId='" + testingSiteId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", formattedStartTime='" + getFormattedStartTime() +
                '}';
    }
}
