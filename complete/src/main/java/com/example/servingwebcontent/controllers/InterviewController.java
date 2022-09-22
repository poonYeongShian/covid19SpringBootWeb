package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.models.api.*;
import com.example.servingwebcontent.enumeration.BookingStatus;
import com.example.servingwebcontent.enumeration.TestType;
import com.example.servingwebcontent.domain.InterviewForm;
import com.example.servingwebcontent.models.apimodel.AuthenticateSingleton;
import com.example.servingwebcontent.models.apimodel.Booking;
import com.example.servingwebcontent.models.apimodel.CovidTest;
import com.example.servingwebcontent.models.apimodel.User;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class InterviewController {
    AuthenticateSingleton authenticateInstance = AuthenticateSingleton.getInstance();

    public List<String> getTestTypeList() {

        List<String> testTypeList = new ArrayList<>();
        for (TestType testType : TestType.values()) {
            testTypeList.add(testType + "");
        }

        return testTypeList;
    }

    @GetMapping("/interview")
    public String getRegister(Model model) {

        // User must be logged in
        if (!authenticateInstance.getIsUserAuthenticated()) {
            return "redirect:/login";
        }

        // User must be a health care worker
        if (!authenticateInstance.getUser().isHealthcareWorker()) {
            return "notAuthorised";
        }

        return "pinInterview";
    }

    @PostMapping("verifyPinCode")
    public String verifyPinCode(@ModelAttribute("interviewForm") InterviewForm interviewForm, Model model)
            throws IOException, InterruptedException, ParseException {

        APIfactory<Booking> bookingFactory = new BookingFactory(System.getenv("API_KEY"));
        Get<Booking> bookingGet = bookingFactory.createGet();

        String pinCode = interviewForm.getPinCode();
        boolean check = false;
        boolean check2 = false;
        boolean check3 = false;

        String patientName = "";
        String patientId = "";

        Collection<Booking> bookingCollection = bookingGet.getApi();

        for (Booking booking : bookingCollection) {
            // Checks if the pin code is the same as the booking PIN
            if (booking.getSmsPin().equals(pinCode)) {
                check = true;
                patientId = booking.getCustomerId();
                patientName = booking.getCustomerName();

                // Checks if the booking status is Completed
                if (booking.getStatus().equals("COMPLETED")) {
                    check2 = true;
                }

                // Checks if booking has done the COVID Test
                if (booking.getTestingDone() == true) {
                    check3 = true;
                }
                break;
            }
        }

        if (!check) {
            model.addAttribute("error", "Pin Code does not exist. Please try again");

            return "pinInterview";
        }

        if (!check2) {
            model.addAttribute("error", "Booking Status is not COMPLETED");

            return "pinInterview";
        }

        if (check3) {
            model.addAttribute("error", "Test has already been performed");

            return "pinInterview";
        }

        User administerer = authenticateInstance.getUser();

        // 1. Interview Form
        InterviewForm interviewForm2 = new InterviewForm();
        model.addAttribute("interviewForm", interviewForm2);

        model.addAttribute("pinCode", pinCode);

        model.addAttribute("patientId", patientId);
        model.addAttribute("patientName", patientName);

        model.addAttribute("administerer", administerer);

        // 3. Get test-type and put it into model
        List<String> testTypeList = getTestTypeList();
        model.addAttribute("testTypeList", testTypeList);

        return "interview";
    }

    @PostMapping("/interview")
    public String submitInterviewForm(@ModelAttribute("interviewForm") InterviewForm interviewForm, Model model)
            throws IOException, InterruptedException, ParseException {

        APIfactory<Booking> bookingFactory = new BookingFactory(System.getenv("API_KEY"));
        Get<Booking> bookingGet = bookingFactory.createGet();
        Collection<Booking> bookingCollection = bookingGet.getApi();

        String pinCode = interviewForm.getPinCode();

        String bookingId = "";

        for (Booking booking : bookingCollection) {
            // Checks if the pin code is the same as booking pin
            if (booking.getSmsPin().equals(pinCode)) {
                bookingId = booking.getBookingId();
                break;
            }
        }

        String patientStatus = "no symptoms";
        if (interviewForm.getHeadache() || interviewForm.getLossTasteAndSmell() || interviewForm.getSoreThroat()
                || interviewForm.getMusclePain() || interviewForm.getShaking() || interviewForm.getCloseContact()) {
            patientStatus = "Headache: " + interviewForm.getHeadache() +
                    ", loss taste and smell: " + interviewForm.getLossTasteAndSmell() +
                    ", sore throat: " + interviewForm.getSoreThroat() +
                    ", muscle pain: " + interviewForm.getMusclePain() +
                    ", shaking: " + interviewForm.getShaking() +
                    ", close contact: " + interviewForm.getCloseContact();
        }

        APIfactory<CovidTest> covidTestFactory = new CovidTestFactory(System.getenv("API_KEY"),
                interviewForm.getTestType(),
                interviewForm.getPatient(), interviewForm.getAdministerer(), bookingId, patientStatus);
        Post covidTestPost = covidTestFactory.createPost();
        // String jsonPost = covidTestPost.postApi();
        List<String> thingsToPost = new ArrayList<>();
        covidTestPost.postApi(thingsToPost);

        // PATCH the symptom into the additional info of the booking api using it
        // booking id
        // change the booking status to completed

        APIfactory<Booking> bookingFactory2 = new BookingFactory(System.getenv("API_KEY"), bookingId, patientStatus,
                BookingStatus.COMPLETED, true);
        Patch bookingPatch = bookingFactory2.createPatch();
        // String returnValue = bookingPatch.patchApi();

        // stuff to patch in booking
        List<String> thingsToPatch = new ArrayList<>();
        thingsToPatch.add("PATSTATUS"); // PATCH PATIENT STATUS
        thingsToPatch.add("STATUS"); // PATCH STATUS
        thingsToPatch.add("TESTDONE"); // PATCH TESTDONE
        String description = "";
        bookingPatch.patchApi(thingsToPatch, description);

        return "testingDone";
    }
}
