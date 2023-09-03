package ru.ngs.summerjob.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Sergey Kovalev
 * Класс основной сущности
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
    /**
     * id транзакции в БД.
     */
    private long id;
    /**
     * Поле даты транзакции.
     */
    private LocalDateTime date;
    /**
     * Поле типа транзакции.
     */
    private TransactionType type;
    /**
     * Поле счёта получателя транзакции.
     */
    private Account accountSender;
    /**
     * Поле счёта отправителя транзакции.
     */
    private Account accountRecipient;
    /**
     * Поле суммы транзакции.
     */
    private double amount;
}
