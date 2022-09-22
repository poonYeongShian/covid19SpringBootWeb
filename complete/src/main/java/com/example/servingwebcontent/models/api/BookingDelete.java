package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.CovidTest;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;

public class BookingDelete extends Delete {
    private final String myApiKey;

    public BookingDelete(String myApiKey) {
        this.myApiKey = myApiKey;

    }
    // TO DO delete the CovidTest by Id first before deleting the Booking by Id

    @Override
    public void deleteApi(String bookingId) throws InterruptedException, ParseException, IOException {
        APIfactory<Booking> bookingFactory = new BookingFactory(myApiKey);
        Get<Booking> bookingGet = bookingFactory.createGet();
        Collection<Booking> bookingCollection = bookingGet.getApi();

        // Delete all covid test of the selected booking
        APIfactory<CovidTest> covidTestFactory = new CovidTestFactory(myApiKey);
        Delete covidTestDelete = covidTestFactory.createDelete();
        for (Booking booking : bookingCollection) {
            if (booking.getBookingId().equals(bookingId)) {
                for (CovidTest covidTest : booking.getCovidTests()) {
                    covidTestDelete.deleteApi(covidTest.getId());
                }

            }
        }

        // Delete the selected booking
        String rootUrl = "https://fit3077.com/api/v2";

        String usersVerifyTokenUrl = rootUrl + "/booking/" + bookingId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(usersVerifyTokenUrl)) // Return a JWT so we can
                // use it in Part 5 later.
                .setHeader("Authorization", myApiKey)
                .header("Content-Type", "application/json") // This header needs to be set when sending
                // a JSON request body.
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("BookingDelete DELETE :> \n----");
        System.out.println(request.uri());
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Full JSON response: " + response.body());

    }
}
