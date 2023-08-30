package ru.ngs.summerjob.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
    private long id;
    private LocalDateTime date;
    private TransactionType type;
    private Account accountSender;
    private Account accountRecipient;
    private double amount;
}
