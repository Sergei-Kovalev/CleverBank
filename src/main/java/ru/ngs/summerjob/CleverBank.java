package ru.ngs.summerjob;

import ru.ngs.summerjob.dao.UserDAOImpl;
import ru.ngs.summerjob.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CleverBank {
    public static void main(String[] args) {

        User userById = new UserDAOImpl().getUserById(1);
        System.out.println(userById);

        System.out.println("Добро пожаловать в приложение Clever-Bank!");
        String login;
        String password;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Пожалуйста введите Ваш логин: ");
        try {
            login = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Пожалуйста введите Ваш пароль: ");
        try {
            password = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        User user = new UserDAOImpl().getUserByLoginAndPassword(login, password);
        while (user.getId() == 0) {
            System.out.println("""
                    Извините пользователя с таким именем/паролем не существует
                    Попробовать ввести снова?
                    1. Да.
                    2. Прекратить работу с программой.
                    """);
            String answer;

            System.out.println("Выберите вариант");
            try {
                answer = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            switch (answer) {
                case ("1") -> {
                    System.out.println("Пожалуйста введите Ваш логин: ");
                    try {
                        login = reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Пожалуйста введите Ваш пароль: ");
                    try {
                        password = reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    user = new UserDAOImpl().getUserByLoginAndPassword(login, password);
                }
                case ("2") -> {
                    System.out.println("Спасибо за программы нашего банка. Всего доброго!");
                    System.exit(0);
                }
                default -> System.out.println("Похоже Вы ввели некорректные данные. Попробуйте еще раз");
            }
        }
        System.out.println("Добро пожаловать " + user.getName() + "!");
    }
}