package ru.ngs.summerjob.controller;

import ru.ngs.summerjob.dao.UserDAOImpl;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;
import ru.ngs.summerjob.entity.TransactionType;
import ru.ngs.summerjob.entity.User;
import ru.ngs.summerjob.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

//TODO будет содержать все использования сервисов нужных для приложения
public class Controller {

    private final static String INCORRECT_CHOICE_FROM_MENU =
            "Похоже Вы ввели некорректные данные. Попробуйте еще раз.";
    private final static String INCORRECT_SUM =
            "Извините, Вы ввели некорректную сумму. Попробуйте еще раз.";
    private final static String CLOSE_APP_CHOICE =
            "Спасибо за программы нашего банка. Всего доброго!";
    UserService userService;
    TransactionTypeService transactionTypeService;
    AccountService accountService;

    public Controller() {
        this.userService = new UserServiceImpl();
        this.transactionTypeService = new TransactionTypeServiceImpl();
        this.accountService = new AccountServiceImpl();
    }

    private User getUserByLoginAndPassword(String login, String password) {
        return userService.getUserByLoginAndPassword(login, password);
    }

    private TransactionType getTransactionTypeById(long id) {
        return transactionTypeService.getTransactionTypeById(id);
    }

    public User userAuthorizationMethod(BufferedReader reader) {
        String login;
        String password;
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

        User user = getUserByLoginAndPassword(login, password);
        while (user.getId() == 0) {
            System.out.println("""
                    Извините пользователя с таким именем/паролем не существует
                    Попробовать ввести снова?
                    1. Да.
                    2. Выход из приложения.
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
                    System.out.println(CLOSE_APP_CHOICE);
                    System.exit(0);
                }
                default -> System.out.println(INCORRECT_CHOICE_FROM_MENU);
            }
        }
        return user;
    }

    public TransactionType selectionOfTransactionType(BufferedReader reader) {
        TransactionType transactionType = null;
        String answer;
        while (transactionType == null) {
            try {
                answer = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch (answer) {
                case ("1") -> transactionType = getTransactionTypeById(3);
                case ("2") -> transactionType = getTransactionTypeById(2);
                case ("3") -> transactionType = getTransactionTypeById(1);
                case ("4") -> {
                    System.out.println(CLOSE_APP_CHOICE);
                    System.exit(0);
                }
                default -> System.out.println(INCORRECT_CHOICE_FROM_MENU);
            }
        }
        return transactionType;
    }

    public Account selectionOfAccount(BufferedReader reader, long userId) {
        Account account = null;
        String answer;

        while (account == null) {
            try {
                answer = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                long accountId = Long.parseLong(answer);
                List<Account> allUserAccounts = accountService.getAccountsByUserId(userId);
                for (Account acc : allUserAccounts) {
                    if (acc.getId() == accountId) {
                        account = accountService.getAccountById(accountId);
                        break;
                    }
                }
            } catch (NumberFormatException ignored) {}
            if (account == null) {
                System.out.println(INCORRECT_CHOICE_FROM_MENU);
            }
        }
        return account;

    }

    public String createAccountsMenu(long userId) {
        StringBuilder menu = new StringBuilder();
        List<Account> allUserAccounts = accountService.getAccountsByUserId(userId);
        if (allUserAccounts.isEmpty()) {
            return "У Вас нет открытых счетов";
        }
        for (Account account : allUserAccounts) {
            menu.append(account.getId() + ". Открыт в " + account.getBank().getName() + System.lineSeparator() +
                    "   Баланс: " + account.getBalance() + " " + account.getCurrency().getName() + System.lineSeparator() +
                    "   Номер счета: " + account.getName() + System.lineSeparator());
        }
        return menu.toString();
    }

    public void replenishmentOfOwnAccount(Account userAccount, TransactionType transactionType, double amount) {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setType(transactionType);
        transaction.setAccountSender(null);
        transaction.setAccountRecipient(userAccount);
        transaction.setAmount(amount);
        System.out.println(transaction);
        //TODO реализовать сохранение транзакции + изменение баланса счета
    }

    public double readAmount(BufferedReader reader) {
        double amount = 0.0;
        String answer;
        int countOfNumbersAfterDot = 0;
        while (amount <= 0 || countOfNumbersAfterDot > 2) {
            try {
                answer = reader.readLine();
                countOfNumbersAfterDot = calculateNumbersAfterDot(answer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                amount = Double.parseDouble(answer);
            } catch (NumberFormatException ignored) {
            }
            if (amount <= 0 || countOfNumbersAfterDot > 2) {
                System.out.println(INCORRECT_SUM);
            }
        }
        return amount;
    }

    private int calculateNumbersAfterDot(String answer) {
        String[] strings = answer.split("\\.");
        if (strings.length > 2) {
            return 3;
        } else if (strings.length > 1) {
            return strings[1].length();
        } else {
            return 0;
        }
    }
}
