package ru.ngs.summerjob.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    //many to one
    private User user;

    //many to one
    private Bank bank;

    // one to one
    private Currency currency;

    //one to many
    private List<Transaction> transactions;

}
