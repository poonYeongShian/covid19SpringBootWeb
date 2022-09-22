package com.example.servingwebcontent.models.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CovidTestDelete extends Delete{
    private final String myApiKey;


    public CovidTestDelete(String myApiKey) {
        this.myApiKey = myApiKey;
    }

    @Override
    public void deleteApi(String covidTestId) throws IOException, InterruptedException {
        String rootUrl = "https://fit3077.com/api/v2";


        // System.out.println(jsonString);
        // Note the POST() method being used here, and the request body is supplied to
        // it.
        // A request body needs to be supplied to this endpoint, otherwise a 400 Bad
        // Request error will be returned.
        String usersVerifyTokenUrl = rootUrl + "/covid-test/"+covidTestId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(usersVerifyTokenUrl)) // Return a JWT so we can
                // use it in Part 5 later.
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json") // This header needs to be set when sending
                // a JSON request body.
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

         System.out.println("CovidTestDelete DELETE :> \n----");
         System.out.println(request.uri());
         System.out.println("Response code: " + response.statusCode());
         System.out.println("Full JSON response: " + response.body());

    }
}
