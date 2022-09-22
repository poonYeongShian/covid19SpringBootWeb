package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.models.api.*;
import com.example.servingwebcontent.enumeration.BookingStatus;
import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;
import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.TestingSite;
import com.example.servingwebcontent.models.apimodel.User;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class AdminPanelController {
    AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();

    // This method get the booking list according to the admin's testing-site
    public List<Booking> getBookingListUsingTestingSite() throws InterruptedException, ParseException, IOException {
        String api = System.getenv("API_KEY");
        APIfactory<TestingSite> factory = new TestingSiteFactory(api);
        Get<TestingSite> testingSiteGet = factory.createGet();
        Collection<TestingSite> testingSites = testingSiteGet.getApi();

        // return the booking from testing-site where the admin is responsible
        User user = authenticateInstance.getUser();
        for (TestingSite testingSite : testingSites) {
            // check if the testing-site id matches the user's testing-site
            if (testingSite.getId().equals(user.getTestingSiteId())) {
                return testingSite.getBookings();
            }
        }
        return null;
    }

    // Get a list of testing-site from api
    public List<TestingSite> getTestingSiteList() {
        List<TestingSite> testingSiteList = new ArrayList<>();

        APIfactory<TestingSite> testingSiteFactory = new TestingSiteFactory(System.getenv("API_KEY"));
        Get<TestingSite> testingSiteGet = testingSiteFactory.createGet();

        try {
            // Testing-site collection
            Collection<TestingSite> testingSiteCollection = testingSiteGet.getApi();

            for (TestingSite testingSite : testingSiteCollection) {
                testingSiteList.add(testingSite);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return testingSiteList;
    }

    @GetMapping("/adminPanel")
    public String getAdmin(Model model) throws InterruptedException, ParseException, IOException {

        // Users must log in first
        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        // Users must be a receptionist
        if (!authenticateInstance.getUser().isReceptionist()) {
            return "notAuthorised";
        }

        List<Booking> bookings = getBookingListUsingTestingSite();

        model.addAttribute("bookings", bookings);

        List<TestingSite> testingSiteList = getTestingSiteList();
        model.addAttribute("testingSiteList", testingSiteList);

        return "adminPanel";
    }

    @RequestMapping("/adminPanelDelete/{id}")
    public String showTestingSite(@PathVariable String id, Model model)
            throws IOException, InterruptedException, ParseException {

        // Delete the testing-site based on the input booking id
        APIfactory<Booking> apiFactory = new BookingFactory(System.getenv("API_KEY"));
        Delete deleteBooking = apiFactory.createDelete();
        deleteBooking.deleteApi(id);

        List<Booking> bookings = getBookingListUsingTestingSite();
        model.addAttribute("bookings", bookings);

        List<TestingSite> testingSiteList = getTestingSiteList();
        model.addAttribute("testingSiteList", testingSiteList);

        return "adminPanel";
    }

    // Modified the Booking Status of that booking to CANCELLED
    @RequestMapping("/adminPanelCancel/{id}/status/{status}")
    public String modifiedStatus(@PathVariable String id, @PathVariable String status, Model model)
            throws IOException, InterruptedException, ParseException {

        String api = System.getenv("API_KEY");

        // Patch the status of the Booking to CANCELLED
        APIfactory<Booking> bookingFactory = new BookingFactory(api, id, BookingStatus.CANCELLED);
        Patch bookingPatch = bookingFactory.createPatch();

        // Add the Status and Admin to thingsToPatch
        List<String> thingsToPatch = new ArrayList<>();
        thingsToPatch.add("STATUS");
        thingsToPatch.add("ADMIN");

        // PATCH with update description
        String description = "Booking has been cancelled";
        bookingPatch.patchApi(thingsToPatch, description);

        List<Booking> bookings = getBookingListUsingTestingSite();
        model.addAttribute("bookings", bookings);

        List<TestingSite> testingSiteList = getTestingSiteList();
        model.addAttribute("testingSiteList", testingSiteList);

        return "adminPanel";
    }

    @RequestMapping(("/adminPanelModified/{id}/testing-site/{second}"))
    public String modifiedTestingSite(@PathVariable String id, @PathVariable String second, Model model)
            throws IOException, InterruptedException, ParseException {

        List<Booking> bookings = getBookingListUsingTestingSite();
        model.addAttribute("bookings", bookings);

        List<TestingSite> testingSiteList = getTestingSiteList();
        model.addAttribute("testingSiteList", testingSiteList);

        return "adminPanel";
    }

    @RequestMapping(("/adminPanelModified/{id}/testing-site/{testsiteid}/bookingTime/{time}"))
    public String modifiedDateTime(@PathVariable String id, @PathVariable String testsiteid, @PathVariable String time,
            Model model)
            throws IOException, InterruptedException, ParseException {

        String api = System.getenv("API_KEY");

        String description = "";

        // If there is a change in testing-site of that booking, patch new testing-site
        if (!testsiteid.equals("testsiteID")) {
            APIfactory<Booking> bookingFactory = new BookingFactory(api, id, null, testsiteid, null);
            Patch bookingPatch = bookingFactory.createPatch();

            List<String> thingsToPatch = new ArrayList<>();
            thingsToPatch.add("TESTSITE");
            thingsToPatch.add("ADMIN");
            description += "Testing-site has been updated ";
            bookingPatch.patchApi(thingsToPatch, description);
            // If there is date, add a comma
            if (!time.equals("date")) {
                description += ",";
            }
        }

        // If there is a change in date of that booking, patch new booking
        if (!time.equals("date")) {
            APIfactory<Booking> bookingFactory = new BookingFactory(api, id, null, null, time);
            Patch bookingPatch = bookingFactory.createPatch();

            List<String> thingsToPatch = new ArrayList<>();
            thingsToPatch.add("TIME");
            thingsToPatch.add("ADMIN");
            // add description after patch update
            description += "Booking time has been updated";
            bookingPatch.patchApi(thingsToPatch, description);
        }

        List<Booking> bookings = getBookingListUsingTestingSite();
        model.addAttribute("bookings", bookings);

        List<TestingSite> testingSiteList = getTestingSiteList();
        model.addAttribute("testingSiteList", testingSiteList);

        return "adminPanel";
    }
}
