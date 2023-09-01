package ru.ngs.summerjob;

import java.time.LocalDateTime;

public class Test {
    public static void main(String[] args) {
        int year = 2023;
        int month = 9;
        LocalDateTime time = LocalDateTime.of(year, month, 1, 0, 0, 0, 999_999_000);

        System.out.println(time);
    }
}
