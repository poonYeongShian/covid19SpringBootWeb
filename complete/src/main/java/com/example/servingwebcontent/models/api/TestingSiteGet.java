package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.models.apimodel.Address;
import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.TestingSite;
import com.example.servingwebcontent.models.apimodel.TestingSiteStatus;
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

public class TestingSiteGet extends Get<TestingSite> {
    private final String myApiKey;

    public TestingSiteGet(String api) {
        this.myApiKey = api;
    }

    @Override
    public Collection<TestingSite> getApi() throws IOException, InterruptedException {

        String rootUrl = "https://fit3077.com/api/v2";
        String usersUrl = rootUrl + "/testing-site?fields=bookings";

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

        // Add testing-site into testing-sites list
        List<TestingSite> testingSites = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            String testingSiteId = (String) json.getJSONObject(i).get("id");
            String testingSiteName = (String) json.getJSONObject(i).get("name");
            String description = (String) json.getJSONObject(i).get("description");
            String websiteUrl = (String) json.getJSONObject(i).get("websiteUrl");
            String phoneNumber = (String) json.getJSONObject(i).get("phoneNumber");
            String createdAt = (String) json.getJSONObject(i).get("createdAt");
            String updatedAt = (String) json.getJSONObject(i).get("updatedAt");
            // Address
            JSONObject addressJson = (JSONObject) json.getJSONObject(i).get("address");
            double latitude = addressJson.getDouble("latitude");
            double longitude = addressJson.getDouble("longitude");
            String unitNumber = addressJson.getString("unitNumber");
            String street = addressJson.getString("street");
            String suburb = addressJson.getString("suburb");
            String state = addressJson.getString("state");
            String postcode = addressJson.getString("postcode");
            Address address = new Address(latitude, longitude, unitNumber, street, suburb, state, postcode);

            // Bookings
            List<Booking> bookings = new ArrayList<>();
            JSONArray bookingsJson = (JSONArray) json.getJSONObject(i).get("bookings");
            String recentUpdateTime = "";
            String updateDesc = "";
            String previousTestSite = "";
            for (int j = 0; j < bookingsJson.length(); j++) {
                String bookingId = (String) bookingsJson.getJSONObject(j).get("id");
                // Customer name, id
                JSONObject customerObj = (JSONObject) bookingsJson.getJSONObject(j).get("customer");
                String customerFName = (String) customerObj.get("familyName");
                String customerLName = (String) customerObj.get("givenName");
                String customerFullName = customerLName + " " + customerFName;
                String customerId = (String) customerObj.get("id");

                // smsPin, status, startTime
                String smsPin = (String) bookingsJson.getJSONObject(j).get("smsPin");
                String status = (String) bookingsJson.getJSONObject(j).get("status");
                String startTime = (String) bookingsJson.getJSONObject(j).get("startTime");
                startTime = startTime.substring(0, 23);

                JSONObject additionalInfoJSON = (JSONObject) bookingsJson.getJSONObject(j).get("additionalInfo");
                try {
                    recentUpdateTime = additionalInfoJSON.getString("recentUpdateTime");
                } catch (Exception exception) {
                    recentUpdateTime = "";
                }
                try {
                    updateDesc = additionalInfoJSON.getString("updateDesc");
                } catch (Exception exception) {
                    updateDesc = "";
                }
                try {
                    previousTestSite = additionalInfoJSON.getString("previousTestSite");
                } catch (Exception exception) {
                    previousTestSite = "";
                }
                Booking booking = new Booking(bookingId, customerId, customerFullName, testingSiteId,
                        testingSiteName, smsPin,
                        startTime, status, null, null, false, false, null, createdAt);
                booking.setRecentUpdateTime(recentUpdateTime);
                booking.setUpdateDesc(updateDesc);
                booking.setPreviousTestSite(previousTestSite);
                bookings.add(booking);
            }

            // Additional Info class
            JSONObject additionalInfoJson = (JSONObject) json.getJSONObject(i).get("additionalInfo");
            String typeOfFacility = additionalInfoJson.getString("typeOfFacility");
            boolean onSiteBookingAndTesting = additionalInfoJson.getBoolean("onSiteBookingAndTesting");
            int waitingTimeInMins = additionalInfoJson.getInt("waitingTimeInMins");
            String openingTime = additionalInfoJson.getString("openingTime");
            String closingTime = additionalInfoJson.getString("closingTime");
            String openOrClosed = additionalInfoJson.getString("openOrClosed");

            TestingSiteStatus additionalInfo = new TestingSiteStatus(typeOfFacility, onSiteBookingAndTesting,
                    waitingTimeInMins, openingTime, closingTime, openOrClosed);

            TestingSite testingSite = new TestingSite(testingSiteId, testingSiteName, description, websiteUrl,
                    phoneNumber, address,
                    additionalInfo, createdAt, updatedAt);
            testingSite.setBookings(bookings);
            testingSites.add(testingSite);
        }
        return testingSites;
    }
}
