package ru.ngs.summerjob;

import ru.ngs.summerjob.controller.Controller;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.TransactionType;
import ru.ngs.summerjob.entity.User;
import ru.ngs.summerjob.utils.InterestTimer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Kovalev
 * Главный класс для старта десктопного приложения.
 * Выполняется в бесконечном цикле до прерывания.
 * (необходимо для отработки утилиты по начислению процентов)
 */
public class CleverBank {
    /**
     * Константа для передачи сообщения об успешном завершении транзакции,
     * совершенной пользователем в приложении.
     */
    private final static String ACCEPTED = "Транзакция прошла успешно";
    /**
     * Константа для приветствия программы в начале выполнения.
     */
    private final static String APP_GREETING = "Добро пожаловать в приложение Clever-Bank!";
    /**
     * Константа для приветствия конкретного пользователя.
     */
    private final static String APP_GREETING_USER = "Добро пожаловать %s!";

    /**
     * Константа для выбора пользователем операций из главного меню.
     */
    private final static String MAIN_MENU = """
                        Что вас интересует?
                        1. Операции со счетом.
                        2. Получить выписку по счету.
                        """;
    /**
     * Константа для меню выбора типа операций.
     */
    private final static String OPERATIONS_MENU = """                
                        Выберите операцию, которая Вас интересует:
                        1. Пополнение счета.
                        2. Снятие средств.
                        3. Перевод на счет другого лица.
                        4. Выход из приложения.
                        """;
    /**
     * Константа для вывода меню с выбором для клиента
     * из открытых им банковских счетов.
     */
    private final static String CHOOSE_ACCOUNT = """
                        Вы выбрали "%s"
                                        
                        В настоящее время у Вас открыты следующие счета:
                        %s
                        Выберите с каким счетом вы хотите произвести данную операцию.
                        """;

    /**
     * Константа для диалога о вводе счета-получателя перевода
     */
    private final static String CHOOSE_RECIPIENT_ACCOUNT = """
                                Пожалуйста введите номер счета получателя.
                                Формат: XXXX XXXX XXXX XXXX XXXX XXXX XXXX
                                """;
    /**
     * Константа для диалога с пользователем о продолжении/прекращении работы с программой.
     */
    private final static String EXIT_CONTINUE = """
                        Желаете совершить продолжить работу с приложением?
                        Введите ДА или YES для продолжения работы либо любой другой текст для выхода из приложения.
                        """;

    /**
     * точка входа в программу
     * @param args приложение не принимает никаких аргументов извне.
     */
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new InterestTimer(), 0, 30, TimeUnit.SECONDS);

        while (true) {
            Controller controller = new Controller();
            System.out.println(APP_GREETING);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            User user = controller.userAuthorizationMethod(reader);
            if (user.getId() != 0) {
                System.out.printf(APP_GREETING_USER + System.lineSeparator(), user.getName());
            } else continue;

            boolean tryAgain = false;
            do {
                System.out.print(MAIN_MENU);
                switch (controller.returnAnswerFromMenuTwoPoints(reader)) {
                    case ("1") -> {
                        System.out.print(OPERATIONS_MENU);
                        TransactionType transactionType = controller.selectionOfTransactionType(reader);
                        if (transactionType.getId() == 0) {
                            continue;
                        }

                        System.out.printf(CHOOSE_ACCOUNT, transactionType.getName(), controller.createAccountsMenu(user.getId()));
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
                                System.out.print(CHOOSE_RECIPIENT_ACCOUNT);
                                Account recepientAccount = controller.readAccountByName(reader, userAccount);

                                System.out.println("Пожалуйста введите сумму для перевода (не может быть меньше 1 копейки):");
                                double amount = controller.readAmount(reader);
                                if (controller.remittance(userAccount, recepientAccount, transactionType, amount)) {
                                    System.out.println(ACCEPTED);
                                }
                            }
                        }
                        System.out.print(EXIT_CONTINUE);
                        tryAgain = controller.isContinue(reader);
                    }
                    case ("2") -> {
                        System.out.printf("""                                   
                        В настоящее время у Вас открыты следующие счета:
                        %s
                        Выберите с каким счетом вы хотите произвести данную операцию.
                        """, controller.createAccountsMenu(user.getId()));
                        Account userAccount = controller.selectionOfAccount(reader, user.getId());

                        controller.accountStatementOutput(reader, userAccount);

                        System.out.print(EXIT_CONTINUE);
                        tryAgain = controller.isContinue(reader);
                    }
                }
            } while (tryAgain);
        }
    }
}