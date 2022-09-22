package com.example.servingwebcontent.controllers;

import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.example.servingwebcontent.models.api.*;
import com.example.servingwebcontent.models.apimodel.*;
import com.example.servingwebcontent.domain.BookingForm;
import com.example.servingwebcontent.domain.RevertBookingForm;
import com.example.servingwebcontent.domain.SearchBookingIDForm;
import com.example.servingwebcontent.enumeration.BookingStatus;
import com.example.servingwebcontent.enumeration.TestType;
import com.google.zxing.WriterException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ModifyBookingController {

    AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();

    public List<TestingSite> getTestingSiteList() {

        // Get testing-sites and put it into model
        APIfactory<TestingSite> testingFactory = new TestingSiteFactory(System.getenv("API_KEY"));
        Get<TestingSite> testingSiteGet = testingFactory.createGet();

        List<TestingSite> testingSiteList = new ArrayList<>();

        try {
            // Testing-site collection
            Collection<TestingSite> testingSiteCollection = testingSiteGet.getApi();

            // Testing Sites which allow on site booking and testing
            for (TestingSite testingSite : testingSiteCollection) {
                if (testingSite.getAdditonalInfo().isOnSiteBookingAndTesting()) {
                    testingSiteList.add(testingSite);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return testingSiteList;
    }

    public List<User> getUserList() {

        // Get the users and put it in a model
        APIfactory<User> userFactory = new UserFactory(System.getenv("API_KEY"));
        Get<User> userGet = userFactory.createGet();

        List<User> userList = new ArrayList<>();

        try {
            // Users collection
            Collection<User> userCollection = userGet.getApi();

            for (User user : userCollection) {
                if (user.isCustomer()) {
                    userList.add(user);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return userList;
    }

    public List<String> getTestTypeList() {

        // Get test-type and put it into model
        List<String> testTypeList = new ArrayList<>();

        for (TestType testType : TestType.values()) {
            testTypeList.add(testType + "");
        }

        return testTypeList;
    }

    public List<Booking> getBookingList() {

        APIfactory<Booking> bookingFactory = new BookingFactory(System.getenv("API_KEY"));
        Get<Booking> bookingGet = bookingFactory.createGet();

        List<Booking> bookingList = new ArrayList<>();

        try {
            // Booking Collection
            Collection<Booking> bookingCollection = bookingGet.getApi();

            for (Booking booking : bookingCollection) {
                bookingList.add(booking);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return bookingList;
    }

    @GetMapping("/profile")
    public String displayProfilePage(Model model) {

        // If the user is not authenticated, redirect to login
        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        // If the user is not a customer, redirect to notAuthorised
        if (!authenticateInstance.getUser().isCustomer()) {
            return "notAuthorised";
        }

        List<Booking> bookingList = getBookingList();

        List<Booking> customerBookingList = new ArrayList<>();

        User currentUser = authenticateInstance.getUser();

        for (Booking booking : bookingList) {
            // Retrieve the particular customer ID
            if (booking.getCustomerId().equals(currentUser.getId())) {
                customerBookingList.add(booking);
            }
        }

        model.addAttribute("bookingList", customerBookingList);

        return "profile";
    }

    @GetMapping("/searchByID")
    public String displaySearchByIDPage(Model model) {

        // If the user is not authenticated, redirect to login
        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        // If the user is not a customer, redirect to notAuthorised
        if (!authenticateInstance.getUser().isCustomer()) {
            return "notAuthorised";
        }

        return "searchByBookingID";
    }

    @PostMapping("/verifyBookingID")
    public String postVerifyBookingID(@ModelAttribute("searchByBookingID") SearchBookingIDForm searchByBookingIDForm,
            Model model) {

        List<Booking> bookingList = getBookingList();
        List<TestingSite> testingSiteList = getTestingSiteList();
        boolean check = false;

        for (Booking booking : bookingList) {
            if (booking.getBookingId().equals(searchByBookingIDForm.getBookingID())) {

                // Returns an error if the covid test is performed
                if (booking.getTestingDone() == true) {
                    model.addAttribute("error", "Invalid Booking. Covid Test has performed");
                    return "searchByBookingID";
                }

                // Returns an error if the booking is cancelled
                if (booking.getCancelBooking() == true) {
                    model.addAttribute("error", "Invalid Booking. Booking is cancelled");
                    return "searchByBookingID";
                }

                // Returns an error if the booking is lapsed
                if (booking.getLapsedBooking() == true) {
                    model.addAttribute("error", "Invalid Booking. Booking is lapsed");
                    return "searchByBookingID";
                }

                model.addAttribute("booking", booking);
                String currentDateTime = booking.getStartTime().substring(0, 16);
                model.addAttribute("currentDateTime", currentDateTime);
                check = true;
                break;
            }
        }

        if (!check) {
            model.addAttribute("error", "Booking ID does not exist");
            return "searchByBookingID";
        }

        model.addAttribute("testingSiteList", testingSiteList);

        return "modifyBooking";
    }

    @RequestMapping("modify/{id}")
    public String modifyBooking(@PathVariable String id, Model model) {

        List<Booking> bookingList = getBookingList();
        List<TestingSite> testingSiteList = getTestingSiteList();

        for (Booking booking : bookingList) {
            // Checks if the booking ID is the same as id
            if (booking.getBookingId().equals(id)) {
                model.addAttribute("booking", booking);
                String currentDateTime = booking.getStartTime().substring(0, 16);
                model.addAttribute("currentDateTime", currentDateTime);
                break;
            }
        }

        model.addAttribute("testingSiteList", testingSiteList);

        return "modifyBooking";
    }

    private boolean isDateSame(Calendar c1, Calendar c2) {
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
    }

    @PostMapping("/modify")
    public String submitModifiedBooking(@ModelAttribute("bookingForm") BookingForm bookingForm, Model model)
            throws IOException, InterruptedException, WriterException, ParseException {

        List<Booking> bookingList = getBookingList();

        boolean check = false;
        boolean check2 = false;

        Booking currentBooking = null;

        List<TestingSite> testingSiteList = getTestingSiteList();
        model.addAttribute("testingSiteList", testingSiteList);

        for (Booking booking : bookingList) {
            if (booking.getBookingId().equals(bookingForm.getBookingID())) {

                // Checks if the testing site is modified
                if (booking.getTestingSiteId().equals(bookingForm.getTestingSite())) {
                    check = true;
                }

                String currentDateTime = booking.getStartTime().substring(0, 16);

                // Checks if the start time is changed
                if (currentDateTime.equals(bookingForm.getTime())) {
                    check2 = true;
                }

                model.addAttribute("booking", booking);
                model.addAttribute("currentDateTime", currentDateTime);

                currentBooking = booking;

                break;
            }
        }

        if (check && check2) {

            model.addAttribute("error", "No changes have been made");

            return "modifyBooking";
        }

        // Check if the chose testing site is the same of any of the existing testing
        // site
        if (currentBooking.getTestingSiteId().equals(bookingForm.getTestingSite())) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date bookingFormDate = (Date) formatter.parse(bookingForm.getTime());
            Date bookingDate = (Date) formatter.parse(currentBooking.getStartTime());

            // Start time of the chosen booking timeslot
            Calendar bookingFormStartTime = Calendar.getInstance();
            bookingFormStartTime.setTime(bookingFormDate);

            // Start time of the existing booking timeslot
            Calendar bookingStartTime = Calendar.getInstance();
            bookingStartTime.setTime(bookingDate);

            // Each timeslot is 10 minutes (10am to 10:05 am)
            // Add 10 minutes interval for each booking
            Calendar bookingEndTime = Calendar.getInstance();
            bookingEndTime.setTime(bookingDate);
            bookingEndTime.add(Calendar.MINUTE, 10);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

            // If the booking date is the same as the existing booking timeslot
            if (isDateSame(bookingFormStartTime, bookingEndTime)) {
                String startTime = timeFormatter.format(bookingStartTime.getTime());
                String endTime = timeFormatter.format(bookingEndTime.getTime());

                // Bookings can be made every 10 minutes after each booking
                if (bookingFormStartTime.getTime().equals(bookingStartTime.getTime())
                        || bookingFormStartTime.getTime().after(bookingStartTime.getTime())
                                && bookingFormStartTime.getTime().before(bookingEndTime.getTime())
                        || bookingFormStartTime.getTime().equals(bookingEndTime.getTime())) {
                    model.addAttribute("error2",
                            "Booking Time " + startTime + " - " + endTime + " has been taken");

                    return "modifyBooking";
                }
            }
        }

        // Timestamp of current changes
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime timeNow = LocalDateTime.now();
        String formattedDateTime = dtf.format(timeNow);

        String currentTime = formattedDateTime.substring(0, 16);

        // Only allow modification if it is a future time
        if (currentTime.equals(currentBooking.getModifiedTimestamp())) {
            model.addAttribute("error3", "You can only modify at a future time");
            return "modifyBooking";
        }

        String oldTimestamp = "";
        String oldTestingSiteId = currentBooking.getTestingSiteId();
        String oldTestingSiteName = currentBooking.getTestingSiteName();
        String oldStartTime = currentBooking.getStartTime();

        // If the modifiedTimestamp is empty, use the createAt Timestamp
        if (currentBooking.getModifiedTimestamp().isEmpty()) {
            oldTimestamp = currentBooking.getCreatedAt();
            oldTimestamp = oldTimestamp.substring(0, 16);
        } else {
            // Otherwise, use the previous modifiedTimestamp
            oldTimestamp = currentBooking.getModifiedTimestamp();
        }

        PastBooking pastBooking = new PastBooking(oldTimestamp, oldTestingSiteId, oldTestingSiteName, oldStartTime);

        List<PastBooking> pastBookings = currentBooking.getPastBookings();

        if (pastBookings.size() < 3) {
            pastBookings.add(pastBooking);
        } else {
            pastBookings.remove(0);
            pastBookings.add(pastBooking);
        }

        // Patch the changes
        String api = System.getenv("API_KEY");
        APIfactory<Booking> bookingFactory = new BookingFactory(api,
                bookingForm.getBookingID(), null, bookingForm.getTestingSite(), bookingForm.getTime(),
                formattedDateTime, pastBookings);
        Patch bookingPatch = bookingFactory.createPatch();

        List<String> thingsToPatch = new ArrayList<>();

        if (!check) {
            thingsToPatch.add("TESTSITE");
        }

        if (!check2) {
            thingsToPatch.add("TIME");
        }

        if (thingsToPatch.contains("TESTSITE") || thingsToPatch.contains("TIME")) {
            thingsToPatch.add("MODIFY");
        }
        String description = "";
        bookingPatch.patchApi(thingsToPatch, description);

        // If the user is a customer, redirect to profile
        if (authenticateInstance.getUser().isCustomer()) {
            return "redirect:/profile";
        }

        return "modifyDone";
    }

    @RequestMapping("cancel/{id}")
    public String cancelBooking(@PathVariable String id, Model model) throws IOException, InterruptedException {

        // Patch the changes
        String api = System.getenv("API_KEY");
        APIfactory<Booking> bookingFactory = new BookingFactory(api, id, true, BookingStatus.CANCELLED);
        Patch bookingPatch = bookingFactory.createPatch();

        List<String> thingsToPatch = new ArrayList<>();

        thingsToPatch.add("CANCEL");
        String description = "Booking has been cancelled";
        bookingPatch.patchApi(thingsToPatch, description);

        // If the user is a customer, redirect to profile
        if (authenticateInstance.getUser().isCustomer()) {
            return "redirect:/profile";
        }

        return "cancelDone";
    }

    @RequestMapping("revert/{id}")
    public String revertBooking(@PathVariable String id, Model model) {

        List<Booking> bookingList = getBookingList();

        for (Booking booking : bookingList) {
            // Get the booking object based on the id
            if (booking.getBookingId().equals(id)) {
                model.addAttribute("booking", booking);
                break;
            }
        }

        return "revertBooking";
    }

    @PostMapping("revertBooking")
    public String submitRevertBooking(@ModelAttribute("revertBookingForm") RevertBookingForm revertBookingForm,
            Model model) throws IOException, InterruptedException {

        String bookingId = revertBookingForm.getBookingId();
        String timestamp = revertBookingForm.getTimestamp();

        List<Booking> bookingList = getBookingList();

        Booking currentBooking = null;

        for (Booking booking : bookingList) {
            if (booking.getBookingId().equals(bookingId)) {
                currentBooking = booking;
                break;
            }
        }

        // List of PastBooking object
        List<PastBooking> pastBookingList = currentBooking.getPastBookings();

        String testingSiteId = "";
        String startTime = "";

        int count = 0;
        for (PastBooking pastBooking : pastBookingList) {
            // Get the pastBooking object based on the timestamp
            if (pastBooking.getTimestamp().equals(timestamp)) {
                testingSiteId = pastBooking.getTestingSiteId();
                startTime = pastBooking.getStartTime();
                break;
            }
            count++;
        }

        // Remove the past Booking from the list
        // pastBookingList.remove(count);
        pastBookingList = pastBookingList.subList(0, count);

        // Timestamp of current changes
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime timeNow = LocalDateTime.now();
        String formattedDateTime = dtf.format(timeNow);

        // Patch the changes
        String api = System.getenv("API_KEY");
        APIfactory<Booking> bookingFactory = new BookingFactory(api,
                bookingId, null,
                testingSiteId,
                startTime,
                formattedDateTime, pastBookingList);
        Patch bookingPatch = bookingFactory.createPatch();

        List<String> thingsToPatch = new ArrayList<>();

        thingsToPatch.add("TESTSITE");
        thingsToPatch.add("TIME");
        thingsToPatch.add("ADMIN");

        if (thingsToPatch.contains("TESTSITE") || thingsToPatch.contains("TIME")) {
            thingsToPatch.add("MODIFY");
        }
        String description = "Booking has been revert";
        bookingPatch.patchApi(thingsToPatch, description);

        // If the user is a customer, redirect to the profile
        if (authenticateInstance.getUser().isCustomer()) {
            return "redirect:/profile";
        }

        return "revertDone";
    }

}