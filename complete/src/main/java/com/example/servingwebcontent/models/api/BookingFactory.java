package com.example.servingwebcontent.models.api;

import java.util.List;

import com.example.servingwebcontent.enumeration.BookingStatus;
import com.example.servingwebcontent.models.apimodel.PastBooking;
import com.example.servingwebcontent.models.apimodel.Booking;

public class BookingFactory implements APIfactory<Booking> {

    private String api;
    private String customerId;
    private String testingSiteId;
    private String startTime;
    private String bookingId;
    private String symptom;
    private BookingStatus bookingStatus;
    private String url;
    private String qrCode;
    private boolean testingDone;
    private boolean cancelBooking;
    private String modifiedTimestamp;
    private List<PastBooking> pastBookings;

    public BookingFactory(String api) {
        this.api = api;
    }

    public BookingFactory(String api, String bookingId, boolean cancelBooking, BookingStatus bookingStatus) {
        this.api = api;
        this.bookingId = bookingId;
        this.cancelBooking = cancelBooking;
        this.bookingStatus = bookingStatus;
    }

    public BookingFactory(String api, String bookingId, BookingStatus bookingStatus) {
        this.api = api;
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
    }

    public BookingFactory(String api, String bookingId, String qrCode, String url) {
        this.api = api;
        this.qrCode = qrCode;
        this.url = url;
        this.bookingId = bookingId;
    }

    public BookingFactory(String api, String bookingId, String symptom, BookingStatus bookingStatus,
            boolean testingDone) {
        this.api = api;
        this.bookingId = bookingId;
        this.symptom = symptom;
        this.bookingStatus = bookingStatus;
        this.testingDone = testingDone;
    }

    public BookingFactory(String api, String bookingId, String customerId, String testingSiteId, String startTime) {
        this.api = api;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.testingSiteId = testingSiteId;
        this.startTime = startTime;
    }

    public BookingFactory(String api, String bookingId, String customerId, String testingSiteId, String startTime,
            String modifiedTimestamp, List<PastBooking> pastBookings) {
        this.api = api;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.testingSiteId = testingSiteId;
        this.startTime = startTime;
        this.modifiedTimestamp = modifiedTimestamp;
        this.pastBookings = pastBookings;
    }

    @Override
    public Get<Booking> createGet() {
        return new BookingGet(api);
    }

    @Override
    public Post createPost() {
        return new BookingPost(api, customerId, testingSiteId, startTime);
    }

    @Override
    public Delete createDelete() {
        return new BookingDelete(api);
    }

    @Override
    public Patch createPatch() {
        return new BookingPatch(api, bookingId, symptom, bookingStatus, qrCode, url, testingSiteId, testingDone,
                startTime, cancelBooking, modifiedTimestamp, pastBookings);
    }
}
