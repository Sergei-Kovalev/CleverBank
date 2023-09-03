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
public class Bank {
    /**
     * id банка в БД.
     */
    private long id;
    /**
     * Поле наименования банка.
     */
    private String name;
}
