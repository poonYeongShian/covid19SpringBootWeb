package com.example.servingwebcontent.domain;

public class InterviewForm {

    private String pinCode;
    private boolean shaking;
    private boolean musclePain;
    private boolean headache;
    private boolean soreThroat;
    private boolean lossTasteAndSmell;
    private boolean closeContact;
    private String administerer;
    private String patient;
    private String testType;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean getShaking() {
        return shaking;
    }

    public void setShaking(boolean shaking) {
        this.shaking = shaking;
    }

    public String getAdministerer() {
        return administerer;
    }

    public void setAdministerer(String administerer) {
        this.administerer = administerer;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public boolean getMusclePain() {
        return musclePain;
    }

    public void setMusclePain(boolean musclePain) {
        this.musclePain = musclePain;
    }

    public boolean getHeadache() {
        return headache;
    }

    public void setHeadache(boolean headache) {
        this.headache = headache;
    }

    public boolean getSoreThroat() {
        return soreThroat;
    }

    public void setSoreThroat(boolean soreThroat) {
        this.soreThroat = soreThroat;
    }

    public boolean getLossTasteAndSmell() {
        return lossTasteAndSmell;
    }

    public void setLossTasteAndSmell(boolean lossTasteAndSmell) {
        this.lossTasteAndSmell = lossTasteAndSmell;
    }

    public boolean getCloseContact() {
        return closeContact;
    }

    public void setCloseContact(boolean closeContact) {
        this.closeContact = closeContact;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    @Override
    public String toString() {
        return "InterviewForm{" +
                "pinCode='" + pinCode + '\'' +
                ", shaking=" + shaking +
                ", musclePain=" + musclePain +
                ", headache=" + headache +
                ", soreThroat=" + soreThroat +
                ", lossTasteAndSmell=" + lossTasteAndSmell +
                ", closeContact=" + closeContact +
                ", administerer='" + administerer + '\'' +
                ", patient='" + patient + '\'' +
                ", testType='" + testType + '\'' +
                '}';
    }
}
