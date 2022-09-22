package com.example.servingwebcontent.observer;

import com.example.servingwebcontent.models.apimodel.Booking;

import java.util.List;

public class CurrentBookingDisplay implements Observer {

    private List<Booking> bookings;

    public void update(List<Booking> bookings) {
        this.bookings = bookings;
        // display();
    }

    public void display() {
        System.out.println("\nBookings:\n"
                + bookings);
    }
}
