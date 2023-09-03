package ru.ngs.summerjob;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {
    public static void main(String[] args) {
        int year = 2023;
        int month = 9;
        LocalDateTime time = LocalDateTime.of(year, month, 1, 0, 0, 0, 999_999_000);

        String fromDateString = "2020-01-01";
        String finishString = fromDateString + " 00:00:00.000";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-L-dd HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.parse(finishString, dateTimeFormatter);
        System.out.println(localDateTime);

        System.out.println(time);
    }
}
