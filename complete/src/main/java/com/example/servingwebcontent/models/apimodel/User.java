package com.example.servingwebcontent.models.apimodel;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String givenName;
    private String familyName;
    private String userName;
    private String phoneNumber;
    private boolean isCustomer;
    private boolean isReceptionist;
    private boolean isHealthcareWorker;

    private List<Booking> bookings;
    private List<CovidTest> testsTaken;
    private List<User> testsAdministered;

    private String testingSiteId;

    public User(String id, String givenName, String familyName, String userName, String phoneNumber, boolean isCustomer,
            boolean isReceptionist, boolean isHealthcareWorker, String testingSiteId) {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.isCustomer = isCustomer;
        this.isReceptionist = isReceptionist;
        this.isHealthcareWorker = isHealthcareWorker;
        this.bookings = new ArrayList<>();
        this.testsTaken = new ArrayList<>();
        this.testsAdministered = new ArrayList<>();
        this.testingSiteId = testingSiteId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFullName() {
        return givenName + " " + familyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public boolean isReceptionist() {
        return isReceptionist;
    }

    public void setReceptionist(boolean receptionist) {
        isReceptionist = receptionist;
    }

    public boolean isHealthcareWorker() {
        return isHealthcareWorker;
    }

    public void setHealthcareWorker(boolean healthcareWorker) {
        isHealthcareWorker = healthcareWorker;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<CovidTest> getTestsTaken() {
        return testsTaken;
    }

    public void addTestsTaken(CovidTest testsTaken) {
        this.testsTaken.add(testsTaken);
    }

    public List<User> getTestsAdministered() {
        return testsAdministered;
    }

    public void addTestsAdministered(User testsAdministered) {
        this.testsAdministered.add(testsAdministered);
    }

    public String getTestingSiteId() {
        return testingSiteId;
    }

    public void setTestingSiteId(String testingSiteId) {
        this.testingSiteId = testingSiteId;
    }

    public String toJSONStringFormat(String userType) {
        return userType + ":{" +
                "id:\"" + id + '\"' +
                ", \"givenName\":\"" + givenName + '\"' +
                ", \"familyName\":\"" + familyName + '\"' +
                ", \"userName\":\"" + userName + '\"' +
                ", \"phoneNumber\":\"" + phoneNumber + '\"' +
                ", \"isCustomer\":" + isCustomer +
                ", \"isReceptionist\":" + isReceptionist +
                ", \"isHealthcareWorker\":" + isHealthcareWorker +
                ", \"additionalInfo\":" + "{}" +
                '}';
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isCustomer=" + isCustomer +
                ", isReceptionist=" + isReceptionist +
                ", isHealthcareWorker=" + isHealthcareWorker +
                ", bookings=" + bookings +
                ", testsTaken=" + testsTaken +
                ", testsAdministered=" + testsAdministered +
                ", testingSiteId='" + testingSiteId + '\'' +
                '}';
    }
}
