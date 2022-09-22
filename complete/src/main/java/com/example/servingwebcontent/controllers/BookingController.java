package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.models.api.*;
import com.example.servingwebcontent.enumeration.BookingStatus;
import com.example.servingwebcontent.enumeration.TestType;
import com.example.servingwebcontent.domain.BookingForm;
import com.example.servingwebcontent.domain.BookingStatusForm;
import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;
import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.TestingSite;
import com.example.servingwebcontent.models.apimodel.User;
import com.google.zxing.WriterException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController {

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
                // Add users who are customers
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

    @GetMapping("/register")
    public String getOnsiteBooking(Model model) {

        // If the user is not authenticated, redirect to login
        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        // If the user is not a receptioniset, redirect to notAuthorised
        if (!authenticateInstance.getUser().isReceptionist()) {
            return "notAuthorised";
        }

        BookingForm bookingForm = new BookingForm();
        model.addAttribute("bookingForm", bookingForm);

        List<TestingSite> testingSiteList = getTestingSiteList();
        model.addAttribute("testingSiteList", testingSiteList);

        List<User> userList = getUserList();
        model.addAttribute("userList", userList);

        List<String> testTypeList = getTestTypeList();
        model.addAttribute("testTypeList", testTypeList);

        // Retrieve the current local time and parse into ISO Format
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime timeNow = LocalDateTime.now();
        model.addAttribute("todayDate", dtf.format(timeNow));

        return "onsiteBooking";
    }

    // Function that compares two calendar objects and check if the date is the same
    private boolean isDateSame(Calendar c1, Calendar c2) {
        return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
    }

    @GetMapping("/bookingStatus")
    public String askForPinCode(Model model) {

        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        return "pinVerification";
    }

    @PostMapping("/submitPinCode")
    public String displayBookingStatus(@ModelAttribute("bookingStatusForm") BookingStatusForm bookingStatusForm,
            Model model) {

        APIfactory<Booking> bookingFactory = new BookingFactory(System.getenv("API_KEY"));

        Get<Booking> bookingGet = bookingFactory.createGet();

        String pinCode = bookingStatusForm.getPinCode();
        boolean exist = false;

        try {
            Collection<Booking> bookingCollection = bookingGet.getApi();

            for (Booking booking : bookingCollection) {
                // If the pincode exists in any of the booking object
                if (booking.getSmsPin().equals(pinCode)) {
                    model.addAttribute("booking", booking);
                    exist = true;
                    break;
                }
            }

        } catch (Exception exception) {
            System.out.println(exception);
        }

        // Returns an error if the pin code does not exist
        if (!exist) {
            model.addAttribute("error", "Pin code does not exist");
            return "pinVerification";
        } else {
            return "bookingStatus";
        }

    }

    @PostMapping("/register")
    public String submitForm(@ModelAttribute("bookingForm") BookingForm bookingForm, Model model)
            throws IOException, InterruptedException, WriterException {

        // Make booking post here
        APIfactory<Booking> bookingFactory = new BookingFactory(
                System.getenv("API_KEY"), null, bookingForm.getCustomerUsername(), bookingForm.getTestingSite(),
                bookingForm.getTime());

        Get<Booking> bookingGet = bookingFactory.createGet();

        try {
            Collection<Booking> bookingCollection = bookingGet.getApi();
            for (Booking booking : bookingCollection) {

                // Some testing sites have null information
                if (booking.getTestingSiteId() == null) {
                    continue;
                }

                // Check if the chose testing site is the same of any of the existing testing
                // site
                if (booking.getTestingSiteId().equals(bookingForm.getTestingSite())) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                    Date bookingFormDate = (Date) formatter.parse(bookingForm.getTime());
                    Date bookingDate = (Date) formatter.parse(booking.getStartTime());

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
                            model.addAttribute("error",
                                    "Booking Time " + startTime + " - " + endTime + " has been taken");

                            BookingForm bookingForm2 = new BookingForm();
                            model.addAttribute("bookingForm", bookingForm2);

                            List<TestingSite> testingSiteList = getTestingSiteList();
                            model.addAttribute("testingSiteList", testingSiteList);

                            List<User> userList = getUserList();
                            model.addAttribute("userList", userList);

                            List<String> testTypeModels = getTestTypeList();
                            model.addAttribute("testTypeModels", testTypeModels);
                            return "register";
                        }
                    }
                }
            }

        } catch (Exception exception) {
            System.out.println(exception);
        }

        Post bookingPost = bookingFactory.createPost();
        List<String> thingsToPost = new ArrayList<>();
        String jsonPost = "";

        // IF IT IS ON-SITE BOOKING THEN IT WILL HAVE UPDATED TIME ATTRIBUTE BEING
        // UPDATED
        if (!bookingForm.isOnHomeBooking()) {
            thingsToPost.add("ADMIN");
        }

        jsonPost = bookingPost.postApi(thingsToPost);

        // Convert booking return JSON string to JSONObject and get the booking id
        JSONObject book = new JSONObject(jsonPost);
        String bookingId = book.get("id") + "";

        // Post Qr code String
        if (bookingForm.isOnHomeBooking()) {
            System.out.println(bookingId);
            String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
            APIfactory<Booking> bookingFactory3 = new BookingFactory(System.getenv("API_KEY"), bookingId,
                    bookingForm.getQr(), url);
            Patch bookingPatch2 = bookingFactory3.createPatch();
            // String returnValue = bookingPatch.patchApi();

            // stuff to patch in booking
            List<String> thingsToPatch = new ArrayList<>();
            thingsToPatch.add("QR"); // PATCH QR
            thingsToPatch.add("URL"); // PATCH URL
            String description = "";
            bookingPatch2.patchApi(thingsToPatch, description);

        } else {
            // OnSite Booking should have COMPLETED as Booking Status
            APIfactory<Booking> bookingFactory2 = new BookingFactory(System.getenv("API_KEY"), bookingId,
                    BookingStatus.COMPLETED);
            Patch bookingPatch = bookingFactory2.createPatch();
            // String returnValue = bookingPatch.patchApi();

            // stuff to patch in booking
            List<String> thingsToPatch = new ArrayList<>();
            thingsToPatch.add("STATUS"); // PATCH STATUS
            String description = "";
            bookingPatch.patchApi(thingsToPatch, description);
        }

        model.addAttribute("pinCode", book.get("smsPin") + "");

        return "pinCode";
    }
}
