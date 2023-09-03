package ru.ngs.summerjob.entity;

import lombok.Data;

/**
 * @author Sergey Kovalev
 * Класс основной сущности
 */
@Data
public class User {
    /**
     * id клиента в БД.
     */
    private long id;
    /**
     * Поле имени клиента.
     */
    private String name;
    /**
     * Поле логина клиента.
     */
    private String login;
    /**
     * Поле пароля клиента
     */
    private String password;
}
