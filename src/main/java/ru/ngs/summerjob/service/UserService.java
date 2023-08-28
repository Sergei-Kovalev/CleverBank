package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.User;

public interface UserService {
    User getUserById(long id);

    User getUserByLoginAndPassword(String login, String password);
}
