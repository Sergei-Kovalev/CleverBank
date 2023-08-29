package ru.ngs.summerjob;

import ru.ngs.summerjob.controller.Controller;
import ru.ngs.summerjob.entity.TransactionType;
import ru.ngs.summerjob.entity.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CleverBank {
    public static void main(String[] args) {

        Controller controller = new Controller();

        System.out.println("Добро пожаловать в приложение Clever-Bank!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        User user = controller.userAuthorizationMethod(reader);

        System.out.printf("""
                Добро пожаловать %s!
                Выберите операцию, которая Вас интересует:
                1. Пополнение счета.
                2. Снятие средств.
                3. Перевод на счет другого лица.
                4. Выход из приложения.
                """, user.getName());
        TransactionType transactionType = controller.selectionOfTransactionType(reader);

        System.out.printf("""
                Вы выбрали "%s"
                
                В настоящее время у Вас открыты следующие счета:
                %s
                Выберите с каким счетом вы хотите произвести данную операцию.
                """, transactionType.getName(), controller.createAccountsMenu(user.getId()));
    }

}