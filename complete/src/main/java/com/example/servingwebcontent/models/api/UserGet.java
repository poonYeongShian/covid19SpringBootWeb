package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.CovidTest;
import com.example.servingwebcontent.models.apimodel.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserGet extends Get<User> {
    private String myApiKey;

    public UserGet(String api) {
        this.myApiKey = api;
    }

    @Override
    public Collection<User> getApi() throws IOException, InterruptedException {
        List<User> users = new ArrayList<>();
        String rootUrl = "https://fit3077.com/api/v2";
        String usersUrl = rootUrl + "/user?fields=bookings.covidTests";

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
        // Create users list
        // Add user into users list

        // Retrieve each user from the user list jsonArray
        for (int i = 0; i < json.length(); i++) {
            String id = (String) json.getJSONObject(i).get("id");
            String givenName = (String) json.getJSONObject(i).get("givenName");
            String familyName = (String) json.getJSONObject(i).get("familyName");
            String userName = (String) json.getJSONObject(i).get("userName");
            String phoneNumber = (String) json.getJSONObject(i).get("phoneNumber");
            boolean isCustomer = (boolean) json.getJSONObject(i).get("isCustomer");
            boolean isReceptionist = (boolean) json.getJSONObject(i).get("isReceptionist");
            boolean isHealthcareWorker = (boolean) json.getJSONObject(i).get("isHealthcareWorker");
            JSONObject addiInfo = (JSONObject) json.getJSONObject(i).get("additionalInfo");
            String adminTestingSiteId = addiInfo.getString("testingSiteId");
            JSONArray bookingJsonArray = json.getJSONObject(i).getJSONArray("bookings");

            List<Booking> bookings = new ArrayList<>();
            // Retrieve each item from the booking list jsonArray
            for (int j = 0; j < bookingJsonArray.length(); j++) {
                String bookingId = (String) bookingJsonArray.getJSONObject(j).get("id");
                String smsPin = (String) bookingJsonArray.getJSONObject(j).get("smsPin");
                String status = (String) bookingJsonArray.getJSONObject(j).get("status");
                String startTime = (String) bookingJsonArray.getJSONObject(j).get("startTime");
                JSONObject testingSite;
                String testingSiteName = "None";
                String testingSiteId = "None";
                // Check whether the testing site is null because Tyler One user has null
                // testing-site
                if (!bookingJsonArray.getJSONObject(j).get("testingSite").equals(null)) {
                    testingSite = (JSONObject) bookingJsonArray.getJSONObject(j).get("testingSite");
                    testingSiteId = (String) testingSite.get("id");
                    testingSiteName = (String) testingSite.get("name");
                }

                // Retrieve each item from the covid-test list jsonArray
                JSONArray covidTests = (JSONArray) bookingJsonArray.getJSONObject(j).get("covidTests");

                List<CovidTest> covidTests1 = new ArrayList<>();
                for (int k = 0; k < covidTests.length(); k++) {
                    String covidTestId = (String) covidTests.getJSONObject(k).get("id");
                    String result = (String) covidTests.getJSONObject(k).get("result");
                    String type = (String) covidTests.getJSONObject(k).get("type");
                    CovidTest covidTest = new CovidTest(covidTestId, type, result);

                    covidTests1.add(covidTest);
                }

                Booking booking = new Booking(bookingId, testingSiteId,
                        testingSiteName, smsPin,
                        startTime, status);
                booking.setCovidTests(covidTests1);
                bookings.add(booking);
            }

            User user = new User(id, givenName, familyName, userName, phoneNumber, isCustomer, isReceptionist,
                    isHealthcareWorker,adminTestingSiteId);
            user.setBookings(bookings);
            users.add(user);
        }
        return users;
    }
}
