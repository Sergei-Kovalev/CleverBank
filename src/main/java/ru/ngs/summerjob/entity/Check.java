package ru.ngs.summerjob.entity;

import lombok.Data;

/**
 * @author Sergey Kovalev
 * Класс основной сущности
 */
@Data
public class Check {
    /**
     * id чека в БД.
     * Используется для получения следующего номера чека.
     */
    private long id;
}
