package ru.ngs.summerjob.entity;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bank {
    private long id;
    private String name;

    //one to many
    private List<Account> accounts;
}
