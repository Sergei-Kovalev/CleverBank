package ru.ngs.summerjob.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {
    private long id;
    private String name;
    private LocalDateTime openingDate;
    private long balance;

    private User user;

    private Bank bank;

    private Currency currency;

}
