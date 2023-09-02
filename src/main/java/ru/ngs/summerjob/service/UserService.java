package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.User;

public interface UserService {
    User getUserById(long id);
    User saveUser(User user);
    User updateUser(User user);
    String deleteUserById(long id);
    User getUserByLoginAndPassword(String login, String password);
}
