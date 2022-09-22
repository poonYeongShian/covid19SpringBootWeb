package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.models.apimodel.PastBooking;
import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.CovidTest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookingGet extends Get<Booking> {

    private final String myApiKey;

    public BookingGet(String myApiKey) {
        this.myApiKey = myApiKey;
    }

    @Override
    public Collection<Booking> getApi() throws IOException, InterruptedException, ParseException {
        List<Booking> bookings = new ArrayList<>();
        String rootUrl = "https://fit3077.com/api/v2";
        String usersUrl = rootUrl + "/booking?fields=covidTests";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(usersUrl))
                .setHeader("Authorization", myApiKey)
                .GET()
                .build();

        HttpResponse<String> response;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Convert String into JSONArray
        JSONArray json = new JSONArray(response.body());

        // Add user into users list

        for (int i = 0; i < json.length(); i++) {
            String bookingId = (String) json.getJSONObject(i).get("id");
            String smsPin = (String) json.getJSONObject(i).get("smsPin");
            String startTime = (String) json.getJSONObject(i).get("startTime");
            String status = (String) json.getJSONObject(i).get("status");
            String createdAt = (String) json.getJSONObject(i).get("createdAt");
            JSONObject additionalInfoJSON = (JSONObject) json.getJSONObject(i).get("additionalInfo");

            // Put covidTests into each Booking
            List<CovidTest> covidTests = new ArrayList<>();
            JSONArray covidTestsJsonArray = (JSONArray) json.getJSONObject(i).get("covidTests");
            for (int j = 0; j < covidTestsJsonArray.length(); j++) {
                String covidTestId = (String) covidTestsJsonArray.getJSONObject(j).get("id");
                CovidTest covidTest = new CovidTest(covidTestId, null, null);
                covidTests.add(covidTest);

            }

            String url = "";
            String qr = "";
            boolean testingDone = false;
            boolean cancelBooking = false;
            String modifiedTimestamp = "";
            String recentUpdateTime = "";

            // Get the URL from the JSON if the attribute exists
            try {
                url = additionalInfoJSON.getString("url");
            } catch (Exception exception) {
                url = "";
            }

            // Get the QR from JSON if the attribute exists
            try {
                qr = additionalInfoJSON.getString("qrCode");
            } catch (Exception exception) {
                qr = "";
            }

            // Get the testingDone from JSON if the attribute exists
            try {
                testingDone = additionalInfoJSON.getBoolean("testingDone");
            } catch (Exception exception) {
                testingDone = false;
            }

            // Get the cancelBooking from JSON if the attribute exists
            try {
                cancelBooking = additionalInfoJSON.getBoolean("cancelBooking");
            } catch (Exception exception) {
                cancelBooking = false;
            }

            // Get the modifiedTimetamp from JSON if the attribute exists
            try {
                modifiedTimestamp = additionalInfoJSON.getString("modifiedTimestamp");
            } catch (Exception exception) {
                modifiedTimestamp = "";
            }

            List<PastBooking> pastBookings = new ArrayList<>();

            try {
                JSONArray pastBookingJSONArray = (JSONArray) additionalInfoJSON.getJSONArray("pastBookings");

                // Retrieve the attributes from the pastBooking Array
                for (int j = 0; j < pastBookingJSONArray.length(); j++) {
                    String oldTimestamp = (String) pastBookingJSONArray.getJSONObject(j).get("timestamp");
                    String oldTestingSiteId = (String) pastBookingJSONArray.getJSONObject(j).get("testingSiteId");
                    String oldTestingSiteName = (String) pastBookingJSONArray.getJSONObject(j).get("testingSiteName");
                    String oldStartTime = (String) pastBookingJSONArray.getJSONObject(j).get("startTime");
                    PastBooking pastBooking = new PastBooking(oldTimestamp, oldTestingSiteId,
                            oldTestingSiteName, oldStartTime);
                    pastBookings.add(pastBooking);
                }
            } catch (Exception exception) {
                pastBookings = new ArrayList<>();
            }

            try {
                recentUpdateTime = additionalInfoJSON.getString("recentUpdateTime");
            } catch (Exception exception) {
                recentUpdateTime = "";
            }
            String testingSiteName = null;
            String testingSiteId = null;

            // If the testing site is not a null object, get the name and id of the testing
            // site
            if (!json.getJSONObject(i).get("testingSite").equals(null)) {
                JSONObject testingSite = (JSONObject) json.getJSONObject(i).get("testingSite");
                testingSiteName = testingSite.getString("name");
                testingSiteId = testingSite.getString("id");
            }

            JSONObject customer = (JSONObject) json.getJSONObject(i).get("customer");
            String customerId = customer.getString("id");
            String customerName = customer.getString("givenName") + " " + customer.getString("familyName");

            Booking booking = new Booking(bookingId, customerId, customerName, testingSiteId, testingSiteName,
                    smsPin,
                    startTime, status, url, qr, testingDone, cancelBooking, modifiedTimestamp, createdAt);
            booking.setRecentUpdateTime(recentUpdateTime);
            booking.setCovidTests(covidTests);
            booking.setPastBookings(pastBookings);
            bookings.add(booking);
        }

        return bookings;
    }

}
