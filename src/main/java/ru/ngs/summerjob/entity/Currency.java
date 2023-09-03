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
public class Currency {
    /**
     * id валюты в БД.
     */
    private long id;
    /**
     * Поле имени валюты.
     */
    private String name;
}
