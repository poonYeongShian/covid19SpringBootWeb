package com.example.servingwebcontent.tool;

// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.Locale;

public class TimeNow {
    public static void main(String[] args) {
        String time = (java.time.LocalDateTime.now().toString()).substring(0, 23) + "Z";
        System.out.println(time);
    }

}
