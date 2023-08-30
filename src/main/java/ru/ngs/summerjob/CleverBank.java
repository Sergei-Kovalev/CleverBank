package ru.ngs.summerjob;

import ru.ngs.summerjob.controller.Controller;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.TransactionType;
import ru.ngs.summerjob.entity.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CleverBank {
    private final static String ACCEPTED = "Транзакция прошла успешно";
    public static void main(String[] args) {

        Controller controller = new Controller();

        System.out.println("Добро пожаловать в приложение Clever-Bank!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        User user = controller.userAuthorizationMethod(reader);
        System.out.printf("Добро пожаловать %s!" + System.lineSeparator(), user.getName());

        do {
            System.out.print("""                
                    Выберите операцию, которая Вас интересует:
                    1. Пополнение счета.
                    2. Снятие средств.
                    3. Перевод на счет другого лица.
                    4. Выход из приложения.
                    """);
            TransactionType transactionType = controller.selectionOfTransactionType(reader);

            System.out.printf("""
                    Вы выбрали "%s"
                                    
                    В настоящее время у Вас открыты следующие счета:
                    %s
                    Выберите с каким счетом вы хотите произвести данную операцию.
                    """, transactionType.getName(), controller.createAccountsMenu(user.getId()));
            Account userAccount = controller.selectionOfAccount(reader, user.getId());

            switch ((int) transactionType.getId()) {
                case 3 -> {
                    System.out.println("Пожалуйста введите сумму для пополнения баланса (не может быть меньше 1 копейки):");
                    double amount = controller.readAmount(reader);
                    if (controller.replenishmentOfOwnAccount(userAccount, transactionType, amount)) {
                        System.out.println(ACCEPTED);
                    }
                }
                case 2 -> {
                    System.out.println("Пожалуйста введите сумму для снятия со счета (не может быть меньше 1 копейки):");
                    double amount = controller.readAmount(reader);
                    if (controller.withdrawFromOwnAccount(userAccount, transactionType, amount)) {
                        System.out.println(ACCEPTED);
                    }
                }
                case 1 -> {
                    System.out.print("""
                            Пожалуйста введите номер счета получателя.
                            Формат: XXXX XXXX XXXX XXXX XXXX XXXX XXXX
                            """);
                    Account recepientAccount = controller.readAccountByName(reader, userAccount);

                    System.out.println("Пожалуйста введите сумму для перевода (не может быть меньше 1 копейки):");
                    double amount = controller.readAmount(reader);
                    if (controller.remittance(userAccount, recepientAccount, transactionType, amount)) {
                        System.out.println(ACCEPTED);
                    }
                }
            }
            System.out.print("""
                    Желаете совершить еще операции по счетам?
                    Введите ДА или YES для продолжения работы либо любой другой текст для выхода из приложения.
                    """);
        } while (controller.isContinue(reader));
    }
}