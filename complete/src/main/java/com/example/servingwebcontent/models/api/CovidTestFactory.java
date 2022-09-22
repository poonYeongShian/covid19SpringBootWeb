package com.example.servingwebcontent.models.api;

import com.example.servingwebcontent.models.apimodel.CovidTest;

public class CovidTestFactory implements APIfactory<CovidTest> {
    private String api;
    private String testType;
    private String patientId;
    private String administererId;
    private String bookingId;
    private String result;
    private String patientStatus;

    public CovidTestFactory(String api, String testType, String patientId, String administererId, String bookingId,
            String patientStatus) {
        this.api = api;
        this.testType = testType;
        this.patientId = patientId;
        this.administererId = administererId;
        this.bookingId = bookingId;
        this.result = "PENDING";
        this.patientStatus = patientStatus;
    }

    public CovidTestFactory(String api) {
        this.api = api;
    }

    @Override
    public Get<CovidTest> createGet() {
        return null;
    }

    @Override
    public Post createPost() {
        return new CovidTestPost(api, testType, patientId, administererId, bookingId, result, patientStatus);
    }

    @Override
    public Delete createDelete() {
        return new CovidTestDelete(api);
    }

    @Override
    public Patch createPatch() {
        return null;
    }
}
