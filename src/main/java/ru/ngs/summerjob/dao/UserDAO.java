package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.User;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see UserDAOImpl
 */
public interface UserDAO {
    /**
     * Метод для получения клиента по id.
     * @see User
     * @param id - id клиента.
     * @return объект клиента.
     */
    User getUserById(long id);
    /**
     * Метод для сохранения клиента.
     * @see User
     * @param user - объект клиента.
     * @return объект сохраненного клиента.
     */
    User saveUser(User user);
    /**
     * Метод для обновления данных клиента.
     * @see User
     * @param user - объект клиента.
     * @return объект измененного клиента.
     */
    User updateUser(User user);
    /**
     * Метод для удаления клиента из БД.
     * @see User
     * @param id - id клиента.
     * @return сообщение об удалении клиента.
     */
    String deleteUserById(long id);
    /**
     * Метод для получения клиента по логину и паролю.
     * @see User
     * @param login - логин клиента.
     * @param password - пароль клиента.
     * @return объект клиента.
     */
    User getUserByLoginAndPassword(String login, String password);
}
