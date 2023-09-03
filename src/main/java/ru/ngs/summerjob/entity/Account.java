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
public class Account {
    /**
     * id счёта в БД.
     */
    private long id;
    /**
     * Поле имени счёта.
     */
    private String name;
    /**
     * Поле даты открытия счёта.
     */
    private LocalDateTime openingDate;
    /**
     * Поле баланса счёта.
     */
    private double balance;
    /**
     * Поле клиента, которому принадлежит счёт.
     */
    private User user;
    /**
     * Поле банка, в котором открыт счёт.
     */
    private Bank bank;
    /**
     * Поле валюты счёта.
     */
    private Currency currency;

}
