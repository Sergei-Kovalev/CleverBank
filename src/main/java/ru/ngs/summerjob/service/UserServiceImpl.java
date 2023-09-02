package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.UserDAO;
import ru.ngs.summerjob.dao.UserDAOImpl;
import ru.ngs.summerjob.entity.User;

public class UserServiceImpl implements UserService {

    UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public User getUserById(long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public User saveUser(User user) {
        return userDAO.saveUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public String deleteUserById(long id) {
        return userDAO.deleteUserById(id);
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        return userDAO.getUserByLoginAndPassword(login, password);
    }
}
