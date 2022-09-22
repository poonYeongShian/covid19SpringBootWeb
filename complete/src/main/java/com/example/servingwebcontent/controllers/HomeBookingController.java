package com.example.servingwebcontent.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.servingwebcontent.models.api.*;
import com.example.servingwebcontent.domain.BookingForm;
import com.example.servingwebcontent.domain.ScanQRForm;
import com.example.servingwebcontent.enumeration.BookingStatus;
import com.example.servingwebcontent.enumeration.TestType;

import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;
import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.TestingSite;
import com.example.servingwebcontent.models.apimodel.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeBookingController {

    AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();

    public List<TestingSite> getTestingSiteList() {
        APIfactory<TestingSite> testingSiteFactory = new TestingSiteFactory(System.getenv("API_KEY"));
        Get<TestingSite> testingSiteGet = testingSiteFactory.createGet();

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
        APIfactory<User> userFactory = new UserFactory(System.getenv("API_KEY"));
        Get<User> userGet = userFactory.createGet();

        List<User> userList = new ArrayList<>();

        try {
            // User collection
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
        // 3. Get test-type and put it into model
        List<String> testTypeList = new ArrayList<>();
        for (TestType testType : TestType.values()) {
            testTypeList.add(testType + "");
        }

        return testTypeList;
    }

    @GetMapping("/homeBooking")
    public String index(Model model) {

        // Users must be logged in
        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        // Users must be a customer
        if (!authenticateInstance.getUser().isCustomer()) {
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

        // Must use yyyy-MM-dd and not yyyy/MM/dd
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        // Retrieve current time and parse it into ISO Format
        LocalDateTime timeNow = LocalDateTime.now();
        model.addAttribute("todayDate", dtf.format(timeNow));

        return "onlineBooking";
    }

    @GetMapping("/scanQRCode")
    public String getScanQRCode(Model model) {

        // Users must be logged in
        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        // Users must be a receptionist
        if (!authenticateInstance.getUser().isReceptionist()) {
            return "notAuthorised";
        }

        return "scanQR";
    }

    @PostMapping("/scanQRCode")
    public String postScanQRCode(@ModelAttribute("interviewForm") ScanQRForm scanQRForm, Model model)
            throws IOException, InterruptedException {

        APIfactory<Booking> bookingFactory = new BookingFactory(System.getenv("API_KEY"));

        Get<Booking> bookingGet = bookingFactory.createGet();

        String qrCode = scanQRForm.getQrCode();

        try {
            Collection<Booking> bookingCollection = bookingGet.getApi();

            for (Booking booking : bookingCollection) {

                // If the booking status is already completed, throw an error
                if (booking.getQr().equals(qrCode)
                        && booking.getStatus().toUpperCase().equals("COMPLETED")) {
                    model.addAttribute("error", "Booking has already been completed");
                    return "scanQR";
                }

                // If the qr code matches any of the booking object, patch the booking status
                if (booking.getQr().equals(qrCode)) {
                    String bookingId = booking.getBookingId();
                    APIfactory<Booking> bookingFactory2 = new BookingFactory(System.getenv("API_KEY"), bookingId,
                            BookingStatus.COMPLETED);
                    Patch bookingPatch = bookingFactory2.createPatch();
                    // String returnValue = bookingPatch.patchApi();

                    // stuff to patch in booking
                    List<String> thingsToPatch = new ArrayList<>();
                    thingsToPatch.add("STATUS"); // PATCH STATUS
                    String description = "";
                    bookingPatch.patchApi(thingsToPatch, description);
                    return "bookingDone";
                }
            }

        } catch (Exception exception) {
            System.out.println(exception);
        }

        model.addAttribute("error", "Qr Code does not exist");
        return "scanQR";
    }
}
