package ru.ngs.summerjob;

import ru.ngs.summerjob.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class CleverBank {
    public static void main(String[] args) {
        System.out.println("Добро пожаловать в приложение Clever-Bank!");
        String login;
        String password;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Пожалуйста введите Ваш логин: ");
            login = reader.readLine();
            System.out.println("Пожалуйста введите Ваш пароль: ");
            password = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = new User();
        user.setId(5);

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/clever_bank_db",
                    "postgres",
                    "193233"

            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {

                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");

                user.setId(id);
                user.setName(name);

                System.out.println(user);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}