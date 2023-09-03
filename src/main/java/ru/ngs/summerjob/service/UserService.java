package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.User;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see UserServiceImpl
 */
public interface UserService {
    /**
     * Метод для получения клиента по id.
     * @see User
     * @param id - id клиента.
     * @return объект клиента.
     */
    User getUserById(long id);
    /**
     * Метод для сохранения клиента.
     * @param user - принимает объект клиента.
     * @return возвращает сохраненный объект клиента.
     * @see User
     */
    User saveUser(User user);
    /**
     * Метод для обновленя данных клиента.
     * @see User
     * @param user - принимает объект клиента.
     * @return возвращает измененный объект клиента.
     */
    User updateUser(User user);
    /**
     * Метод удаления клиента по id.
     * @param id - принимает id клиента для удаления.
     * @return возвращает строку об успешном удалении.
     */
    String deleteUserById(long id);
    /**
     * Метод для получения клиента по логину и паролю.
     * @param login - логин клиента.
     * @param password - пароль клиента.
     * @return возвращает объект клиента из БД.
     * @see User
     */
    User getUserByLoginAndPassword(String login, String password);
}
