package com.example.servingwebcontent.models.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class BookingPost extends Post {
        private final String myApiKey;
        private final String customerId;
        private final String testingSiteId;
        private final String startTime;

        public BookingPost(String api, String customerId, String testingSiteId, String startTime) {
                this.myApiKey = api;
                this.customerId = customerId;
                this.testingSiteId = testingSiteId;
                this.startTime = startTime;
        }

        @Override
        public String postApi(List<String> thingsToPost) throws IOException, InterruptedException {
                String rootUrl = "https://fit3077.com/api/v2";

                String jsonString = "{" +
                                "\"customerId\":\"" + customerId + "\"," +
                                "\"testingSiteId\":\"" + testingSiteId + "\"," +
                                "\"startTime\":\"" + startTime + "\"";
                                if(thingsToPost.contains("ADMIN")){
                                        jsonString += ",\"additionalInfo\":" + "{ "
                                                   + "\"recentUpdateTime\":\""
                                                + (java.time.LocalDateTime.now().toString()).substring(0, 23)
                                                + "Z" + "\""
                                                + "}";
                                }

                jsonString += "}";
                // Note the POST() method being used here, and the request body is supplied to
                // it.
                // A request body needs to be supplied to this endpoint, otherwise a 400 Bad
                // Request error will be returned.
                String usersVerifyTokenUrl = rootUrl + "/booking";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder(URI.create(usersVerifyTokenUrl)) // Return a JWT so we can
                                                                                              // use it in Part 5 later.
                                .setHeader("Authorization", myApiKey)
                                .header("Content-Type", "application/json") // This header needs to be set when sending
                                                                            // a JSON request body.
                                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                                .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Booking POST :> \n----");
                System.out.println(request.uri());
                System.out.println("Response code: " + response.statusCode());
                System.out.println("Full JSON response: " + response.body());
                return response.body();
        }
}
