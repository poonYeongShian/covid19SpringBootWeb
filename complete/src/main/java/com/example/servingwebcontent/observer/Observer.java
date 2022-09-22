package com.example.servingwebcontent.observer;

import com.example.servingwebcontent.models.apimodel.Booking;

import java.util.List;

public interface Observer {
    public void update(List<Booking> bookings);
}

