package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.enumeration.BookingStatus;
import com.example.servingwebcontent.models.apimodel.PastBooking;
import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BookingPatch extends Patch {
        private final String myApiKey;
        private final String bookingId;
        private final String symptom;
        private final BookingStatus bookingStatus;
        private final String qrCode;
        private final String url;
        private final String testingSiteId;
        private final boolean testingDone;
        private final String startTime;
        private final boolean cancelBooking;
        private final String modifiedTimestamp;
        private final List<PastBooking> pastBookings;

        public BookingPatch(String myApiKey, String bookingId, String symptom, BookingStatus bookingStatus,
                        String qrCode,
                        String url, String testingSiteId, boolean testingDone, String startTime, boolean cancelBooking,
                        String modifiedTimestamp, List<PastBooking> pastBookings) {
                this.myApiKey = myApiKey;
                this.bookingId = bookingId;
                this.symptom = symptom;
                this.bookingStatus = bookingStatus;
                this.qrCode = qrCode;
                this.url = url;
                this.testingSiteId = testingSiteId;
                this.testingDone = testingDone;
                this.startTime = startTime;
                this.cancelBooking = cancelBooking;
                this.modifiedTimestamp = modifiedTimestamp;
                this.pastBookings = pastBookings;
        }

        @Override
        public String patchApi(List<String> thingsToPatch, String description)
                        throws IOException, InterruptedException {
                HttpClient client = HttpClient.newHttpClient();
                String rootUrl = "https://fit3077.com/api/v2";
                String usersUrl = rootUrl + "/booking/" + bookingId;
                String jsonString;
                if (thingsToPatch.contains("QR") && thingsToPatch.contains("URL")) {
                        jsonString = "{" +
                                        "\"additionalInfo\":" + "{ " +
                                        "\"qrCode\":\"" + qrCode + "\"," +
                                        "\"url\":\"" + url + "\"" +
                                        "}" + "}";
                } else if (thingsToPatch.contains("TESTDONE")) {
                        jsonString = "{" +
                                        "\"additionalInfo\":" + "{ " +
                                        "\"testingDone\":\"" + testingDone + "\"" +
                                        "}" + "}";
                } else if (thingsToPatch.contains("STATUS")) {
                        jsonString = "{" + "\"status\":\"" + bookingStatus + "\"";
                        if (thingsToPatch.contains("ADMIN")) {
                                jsonString += ",\"additionalInfo\":" + "{ "
                                                + "\"recentUpdateTime\":\""
                                                + (java.time.LocalDateTime.now().toString()).substring(0, 23)
                                                + "Z" + "\"";
                                jsonString += ",\"updateDesc\":\""
                                                + description + "\"";
                        }
                        jsonString += "}}";
                } else if (thingsToPatch.contains("TESTSITE")) {
                        jsonString = "{" +
                                        "\"testingSiteId\":\"" + testingSiteId + "\",";

                        if (thingsToPatch.contains("TIME")) {
                                jsonString += "\"startTime\":\"" + startTime + "\",";
                        }

                        jsonString += "\"additionalInfo\":" + "{ ";

                        if (thingsToPatch.contains("ADMIN")) {
                                AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();
                                jsonString += "\"recentUpdateTime\":\""
                                                + (java.time.LocalDateTime.now().toString()).substring(0, 23)
                                                + "Z" + "\"";
                                jsonString += ",\"updateDesc\":\""
                                                + description + "\"";
                                jsonString += ",\"previousTestSite\":\""
                                                + authenticateInstance.getUser().getTestingSiteId() + "\"";
                        }

                        if (thingsToPatch.contains("MODIFY")) {
                                if (thingsToPatch.contains("ADMIN")) {
                                        jsonString += ",";
                                }
                                jsonString += "\"modifiedTimestamp\":\"" + modifiedTimestamp + "\"," +
                                                "\"pastBookings\":" + "[";

                                int count = 0;
                                for (PastBooking pastBooking : pastBookings) {
                                        jsonString += "{" +
                                                        "\"timestamp\":\"" + pastBooking.getTimestamp() + "\"," +
                                                        "\"testingSiteId\":\"" + pastBooking.getTestingSiteId() + "\","
                                                        +
                                                        "\"testingSiteName\":\"" + pastBooking.getTestingSiteName()
                                                        + "\"," +
                                                        "\"startTime\":\"" + pastBooking.getStartTime() + "\"";
                                        if (count != pastBookings.size() - 1) {
                                                jsonString += "},";
                                        } else {
                                                jsonString += "}";
                                        }

                                        count++;
                                }

                                jsonString += "]";
                        }

                        jsonString += "}" + "}";
                } else if (thingsToPatch.contains("TIME")) {
                        jsonString = "{" +
                                        "\"startTime\":\"" + startTime + "\",";

                        if (thingsToPatch.contains("TESTSITE")) {
                                jsonString += "\"testingSiteId\":\"" + testingSiteId + "\",";
                        }

                        jsonString += "\"additionalInfo\":" + "{ ";

                        if (thingsToPatch.contains("ADMIN")) {
                                jsonString += "\"recentUpdateTime\":\""
                                                + (java.time.LocalDateTime.now().toString()).substring(0, 23)
                                                + "Z" + "\"";
                                jsonString += ",\"updateDesc\":\""
                                                + description + "\"";
                        }
                        if (thingsToPatch.contains("MODIFY")) {
                                jsonString += "\"modifiedTimestamp\":\"" + modifiedTimestamp + "\"," +
                                                "\"pastBookings\":" + "[";

                                int count = 0;
                                for (PastBooking pastBooking : pastBookings) {
                                        jsonString += "{" +
                                                        "\"timestamp\":\"" + pastBooking.getTimestamp() + "\"," +
                                                        "\"testingSiteId\":\"" + pastBooking.getTestingSiteId() + "\","
                                                        +
                                                        "\"testingSiteName\":\"" + pastBooking.getTestingSiteName()
                                                        + "\"," +
                                                        "\"startTime\":\"" + pastBooking.getStartTime() + "\"";
                                        if (count != pastBookings.size() - 1) {
                                                jsonString += "},";
                                        } else {
                                                jsonString += "}";
                                        }

                                        count++;
                                }

                                jsonString += "]";
                        }
                        jsonString += "}" + "}";

                } else if (thingsToPatch.contains("CANCEL")) {
                        jsonString = "{" +
                                        "\"status\":\"" + bookingStatus + "\"," +
                                        "\"additionalInfo\":" + "{ " +
                                        "\"cancelBooking\":\"" + cancelBooking + "\"";
                        jsonString += ",\"recentUpdateTime\":\""
                                        + (java.time.LocalDateTime.now().toString()).substring(0, 23)
                                        + "Z" + "\"";
                        jsonString += ",\"updateDesc\":\""
                                        + description + "\"";

                        jsonString += "}" + "}";
                } else {
                        jsonString = "{" +
                                        "\"status\":\"" + bookingStatus + "\"," +
                                        "\"additionalInfo\":" + "{ " +
                                        "\"symptom\":\"" + symptom + "\"" +
                                        "}" + "}";
                }
                System.out.println(jsonString);
                HttpRequest.BodyPublisher jsonPayload = HttpRequest.BodyPublishers.ofString(jsonString);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(usersUrl))
                                .method("PATCH", jsonPayload)
                                .setHeader("Authorization", myApiKey)
                                .header("Content-Type", "application/json")
                                .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Booking Patch :> \n----");
                System.out.println(request.uri());
                System.out.println("Response code: " + response.statusCode());
                System.out.println("Full JSON response: " + response.body());
                return response.body();
        }
}
