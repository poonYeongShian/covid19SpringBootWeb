package com.example.servingwebcontent.models.apimodel;

import java.util.ArrayList;
import java.util.List;

public class TestingSite {

    private String id;
    private String name;
    private String description;
    private String websiteUrl;
    private String phoneNumber;
    private Address address;
    private List<Booking> bookings;
    private TestingSiteStatus additonalInfo;
    private String createdAt;
    private String updatedAt;

    public TestingSite(String id, String name, String description, String websiteUrl, String phoneNumber,
            Address address, TestingSiteStatus additonalInfo, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bookings = new ArrayList<>();
        this.additonalInfo = additonalInfo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public TestingSiteStatus getAdditonalInfo() {
        return additonalInfo;
    }

    public void setAdditonalInfo(TestingSiteStatus additonalInfo) {
        this.additonalInfo = additonalInfo;
    }

    public String toJSONStringFormat() {
        return "testingSite:{" +
                "\"id\":\"" + id + '\"' +
                ", \"name\":\"" + name + '\"' +
                ", \"description\":\"" + description + '\"' +
                ", \"websiteUrl\":\"" + websiteUrl + '\"' +
                ", \"phoneNumber\":\"" + phoneNumber + '\"' +
                "," + address.toJSONStringFormat() +
                ", \"createdAt\":\"" + createdAt + '\"' +
                ", \"updatedAt\":\"" + updatedAt + '\"' +
                "," + additonalInfo.toJSONStringFormat() +
                '}';
    }

    @Override
    public String toString() {
        return "TestingSite{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address +
                ", bookings=" + bookings +
                ", additonalInfo=" + additonalInfo +
                '}';
    }
}
