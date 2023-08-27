package ru.ngs.summerjob.entity;

import lombok.*;

import java.util.List;

@Data
public class User {
    private long id;
    private String name;
    private List<Account> accounts;
    private String login;
    private String password;
}
