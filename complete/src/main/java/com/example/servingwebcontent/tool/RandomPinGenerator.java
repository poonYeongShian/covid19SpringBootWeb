package com.example.servingwebcontent.tool;

import java.util.Random;

public class RandomPinGenerator {
    private final String pin;
    public final static int NUMBER_OF_VALUES = 999999;

    public RandomPinGenerator() {
        Random rndGenerator = new Random();
        this.pin = String.format("%06d", rndGenerator.nextInt(NUMBER_OF_VALUES));;
    }

    public String getPin() {
        return pin;
    }
}

