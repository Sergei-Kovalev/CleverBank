package ru.ngs.summerjob.entity;

import lombok.*;

/**
 * @author Sergey Kovalev
 * Класс основной сущности
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionType {
    /**
     * id типа транзакции в БД.
     */
    private long id;
    /**
     * Поле имени типа транзакции.
     */
    private String name;
}
