package com.example.demo1.Helpers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VisitManagmentHelper {

    public static LocalDateTime createDateFromString(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return LocalDateTime.parse(str, formatter);
    }


}
