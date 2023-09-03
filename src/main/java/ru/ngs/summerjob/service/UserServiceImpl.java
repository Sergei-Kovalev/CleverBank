package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.UserDAO;
import ru.ngs.summerjob.dao.UserDAOImpl;
import ru.ngs.summerjob.entity.User;

/**
 * @author Sergey Kovalev
 * Реализация интерфейса и методов класса:
 * @see UserService
 */
public class UserServiceImpl implements UserService {
    /**
     * Это поле для загрузки dao получающего необходимы данные из БД.
     * @see UserDAO
     */
    UserDAO userDAO;

    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов.
     */
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Имплементация метода возвращающего клиента по id.
     * @param id - id клиента.
     * @return - объект класса:
     * @see User
     */
    @Override
    public User getUserById(long id) {
        return userDAO.getUserById(id);
    }

    /**
     * Имплементация метода сохраняющего объект клиента.
     * @see User
     * @param user - принимает объект клиента.
     * @return возвращает объект клиента сохраненный в базе данных (с заполненным из БД id).
     */
    @Override
    public User saveUser(User user) {
        return userDAO.saveUser(user);
    }

    /**
     * Имплементация метода обновления данных клиента.
     * @see User
     * @param user - принимает объект клиента, который необходимо изменить.
     * @return возвращает объект клиента измененный в базе дынных (с заполненными из БД данными).
     */
    @Override
    public User updateUser(User user) {
        return userDAO.updateUser(user);
    }

    /**
     * Имплементация метода удаления клиента по id.
     * @param id - параметр принимающий id клиента.
     * @return возвращает строку об успешном удалении клиента.
     */
    @Override
    public String deleteUserById(long id) {
        return userDAO.deleteUserById(id);
    }

    /**
     * Имплементация метода поиска клиента по логину и паролю.
     * @see User
     * @param login - поле логина клиента.
     * @param password - поле пароля клиента.
     * @return возвращает объект клиента с указанными логином и паролем.
     */
    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        return userDAO.getUserByLoginAndPassword(login, password);
    }
}
