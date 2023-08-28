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
        return null;
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password) {
        return userDAO.getUserByLoginAndPassword(login, password);
    }
}
