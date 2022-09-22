package com.example.servingwebcontent.models.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class CovidTestPost extends Post {
        private String myApiKey;
        private String testType;
        private String patientId;
        private String administererId;
        private String bookingId;
        private String result;
        private String patientStatus;

        public CovidTestPost(String api, String testType, String patientId, String administererId, String bookingId,
                        String result, String patientStatus) {
                this.myApiKey = api;
                this.testType = testType;
                this.patientId = patientId;
                this.administererId = administererId;
                this.bookingId = bookingId;
                this.result = result;
                this.patientStatus = patientStatus;
        }

        @Override
        public String postApi(List<String> thingsToPost) throws IOException, InterruptedException {
                String rootUrl = "https://fit3077.com/api/v2";

                String jsonString = "{" +
                                "\"type\":\"" + testType + "\"," +
                                "\"patientId\":\"" + patientId + "\"," +
                                "\"administererId\":\"" + administererId + "\"," +
                                "\"bookingId\":\"" + bookingId + "\"," +
                                "\"result\":\"" + result + "\"," +
                                "\"additionalInfo\":" + "{\"patientStatus\":\"" + patientStatus + "\"" + "}" +
                                "}";

                // System.out.println(jsonString);
                // Note the POST() method being used here, and the request body is supplied to
                // it.
                // A request body needs to be supplied to this endpoint, otherwise a 400 Bad
                // Request error will be returned.
                String usersVerifyTokenUrl = rootUrl + "/covid-test";
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
