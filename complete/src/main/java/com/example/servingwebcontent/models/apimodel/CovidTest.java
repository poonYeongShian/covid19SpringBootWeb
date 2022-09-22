package com.example.servingwebcontent.models.apimodel;

public class CovidTest {

    private String id;
    private String testType;
    private String result;

    public CovidTest(String id, String testType, String result) {
        this.id = id;
        this.testType = testType;
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CovidTest{" +
                "id='" + id + '\'' +
                ", testType=" + testType +
                ", result=" + result +
                '}';
    }
}
