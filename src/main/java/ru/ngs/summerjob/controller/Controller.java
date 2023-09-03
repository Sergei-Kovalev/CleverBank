package ru.ngs.summerjob.controller;

import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;
import ru.ngs.summerjob.entity.TransactionType;
import ru.ngs.summerjob.entity.User;
import ru.ngs.summerjob.service.*;
import ru.ngs.summerjob.utils.AbstractStatementGenerator;
import ru.ngs.summerjob.utils.AccountStatementGenerator;
import ru.ngs.summerjob.utils.CheckGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Класс-контроллер для консольного приложения.
 * Основной класс-клиент:
 * @see ru.ngs.summerjob.CleverBank
 */
public class Controller {
    /**
     * Константа для вывода сообщения о неверных введенных данных в меню.
     */
    private final static String INCORRECT_CHOICE_FROM_MENU =
            "Похоже Вы ввели некорректные данные. Попробуйте еще раз.";
    /**
     * Константа для вывода сообщения о вводе неверной суммы.
     */
    private final static String INCORRECT_SUM =
            "Извините, Вы ввели некорректную сумму. Попробуйте еще раз.";
    /**
     * Константа для вывода сообщения о вводе неверного формата счёта.
     */
    private final static String INCORRECT_ACCOUNT_FORMAT =
            "Извините, Вы ввели счёт неверного формата. Попробуйте еще раз.";
    /**
     * Константа для вывода сообщения о вводе счёта которого не существует в базе данных.
     */
    private final static String INCORRECT_ACCOUNT_FROM_DB =
            "Извините, такого счета не существует, возможно Вы ошиблись при вводе. Попробуйте еще раз.";
    /**
     * Константа для вывода сообщения о попытке отправки перевода со счета на него самого.
     */
    private final static String INCORRECT_ACCOUNT_YOURSELF =
            "Извините, Вы ввели счет получатель тот же что и отправитель - это не имеет смысла :). Попробуйте еще раз.";
    /**
     * Константа для вывода сообщения о завершении работы с программой.
     */
    private final static String CLOSE_APP_CHOICE =
            """
                    Спасибо за программы нашего банка. Всего доброго!
                    __________________________________________________________________________________________
                                        
                    """;
    /**
     * Константа для вывода сообщения о выборе периода.
     */
    private final static String CHOOSE_PERIOD = """
            Пожалуйста выберите какой формат выписки Вас интересует:
            1. За месяц.
            2. За год.
            3. За весь период.
            """;
    /**
     * Константа для вывода сообщения о вводе года.
     */
    private final static String CHOOSE_YEAR = """
            Пожалуйста введите год (должен быть между 1970 и %s):
            """;
    /**
     * Константа для вывода сообщения о выборе месяца.
     */
    private final static String CHOOSE_MONTH = """
            Пожалуйста, выберите месяц (по номеру):
            1. Январь           7. Июль
            2. Февраль          8. Август
            3. Март             9. Сентябрь
            4. Апрель          10. Октябрь
            5. Май             11. Ноябрь
            6. Июнь            12. Декабрь
            """;

    /**
     * Это поле для загрузки сервиса.
     * @see UserService
     */
    UserService userService;
    /**
     * Это поле для загрузки сервиса.
     * @see TransactionTypeService
     */
    TransactionTypeService transactionTypeService;
    /**
     * Это поле для загрузки сервиса.
     * @see AccountService
     */
    AccountService accountService;
    /**
     * Это поле для загрузки сервиса.
     * @see TransactionService
     */
    TransactionService transactionService;
    /**
     * Это поле для загрузки сервиса.
     * @see CheckGenerator
     */
    CheckGenerator checkGenerator;
    /**
     * Это поле для загрузки абстрактного класса.
     * @see AccountStatementGenerator
     */
    AbstractStatementGenerator accountStatementGenerator;
    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов и абстрактных классов.
     */
    public Controller() {
        this.userService = new UserServiceImpl();
        this.transactionTypeService = new TransactionTypeServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.checkGenerator = new CheckGenerator();
        this.accountStatementGenerator = new AccountStatementGenerator();
    }

    /**
     * Получает объект класса клиент по логину и паролю.
     * @see User
     * @param login - логин клиента.
     * @param password - пароль клиента.
     * @return объект класса клиент.
     */
    private User getUserByLoginAndPassword(String login, String password) {
        return userService.getUserByLoginAndPassword(login, password);
    }
    /**
     * Получает объект класса тип транзакции по id.
     * @see TransactionType
     * @param id - id клиента.
     * @return объект класса тип транзакции.
     */
    private TransactionType getTransactionTypeById(long id) {
        return transactionTypeService.getTransactionTypeById(id);
    }

    /**
     * Метод для авторизации клиента
     * @see User
     * Исполняется пока клиент не введёт верный логин и пароль либо откажется пробовать снова
     * @param reader - объект для чтения данных из консоли.
     * @return объект класса клиент.
     */
    public User userAuthorizationMethod(BufferedReader reader) {
        User user = new User();
        user.setId(0);

        boolean tryAgain;
        do {
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

            user = getUserByLoginAndPassword(login, password);

            if (user.getId() == 0) {
                System.out.println("Извините клиента с таким именем/паролем не существует");
                System.out.print("""
                        Желаете попробовать снова?
                        Введите ДА или YES для продолжения работы либо любой другой текст для выхода из приложения.
                        """);
            } else break;
            tryAgain = isContinue(reader);
        } while (tryAgain);
        return user;
    }

    /**
     * Метод для выбора клиентом в меню возможных типов транзакций по счёту.
     * @see TransactionType
     * @param reader - объект для чтения данных из консоли.
     * @return тип транзакции, которую выбрал клиент
     */
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
                    transactionType = new TransactionType();
                    transactionType.setId(0);
                }
                default -> System.out.println(INCORRECT_CHOICE_FROM_MENU);
            }
        }
        return transactionType;
    }

    /**
     * Метод для выбора клиентом счёта с которым он хочет производить операции.
     * @see Account
     * @param reader - объект для чтения данных из консоли.
     * @param userId - id клиента.
     * @return объект счёта.
     */
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
            } catch (NumberFormatException ignored) {
            }
            if (account == null) {
                System.out.println(INCORRECT_CHOICE_FROM_MENU);
            }
        }
        return account;
    }

    /**
     * Метод для создания меню из всех счетов клиента.
     * Может вернуть сообщение об отсутствии открытых счетов.
     * @param userId - id клиента
     * @return строку с меню сгенерированному из всех счетов клиента.
     */
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

    /**
     * Метод пополнения собственного счёта клиентом.
     * По завершении генерирует чек, хранящийся в папке /check проекта
     * @param userAccount - объект счёта клиента.
     * @param transactionType - объект типа транзакции.
     * @param amount - сумма транзакции.
     * @return true если операция завершена успешно.
     */
    public boolean replenishmentOfOwnAccount(Account userAccount, TransactionType transactionType, double amount) {
        Transaction transaction = new Transaction();
        fillConstantFieldsForTransaction(transactionType, amount, transaction);

        transaction.setAccountSender(null);
        transaction.setAccountRecipient(userAccount);
        checkGenerator.saveCheckInFile(transaction);
        return transactionService.saveTransaction(transaction);
    }
    /**
     * Метод снятия средств со счёта клиентом.
     * По завершении генерирует чек, хранящийся в папке /check проекта
     * @param userAccount - объект счёта клиента.
     * @param transactionType - объект типа транзакции.
     * @param amount - сумма транзакции.
     * @return true если операция завершена успешно.
     */
    public boolean withdrawFromOwnAccount(Account userAccount, TransactionType transactionType, double amount) {
        Transaction transaction = new Transaction();
        fillConstantFieldsForTransaction(transactionType, amount, transaction);

        transaction.setAccountSender(null);
        transaction.setAccountRecipient(userAccount);
        checkGenerator.saveCheckInFile(transaction);
        return transactionService.saveTransaction(transaction);
    }
    /**
     * Метод пополнения перевода денег клиентом на счёт другого лица.
     * По завершении генерирует чек, хранящийся в папке /check проекта
     * @param userAccount - объект счёта клиента.
     * @param transactionType - объект типа транзакции.
     * @param recepientAccount - объект клиента-получателя транзакции.
     * @param amount - сумма транзакции.
     * @return true если операция завершена успешно.
     */
    public boolean remittance(Account userAccount, Account recepientAccount, TransactionType transactionType, double amount) {
        Transaction transaction = new Transaction();
        fillConstantFieldsForTransaction(transactionType, amount, transaction);

        transaction.setAccountSender(userAccount);
        transaction.setAccountRecipient(recepientAccount);
        checkGenerator.saveCheckInFile(transaction);
        return transactionService.saveTransaction(transaction);
    }

    /**
     * Метод для заполнения стандартных полей при генерации транзакции
     * @param transactionType - объект типа транзакции.
     * @param amount - сумма транзакции.
     * @param transaction - транзакция для заполнения.
     */
    private static void fillConstantFieldsForTransaction(TransactionType transactionType, double amount, Transaction transaction) {
        transaction.setDate(LocalDateTime.now());
        transaction.setType(transactionType);
        transaction.setAmount(amount);
    }

    /**
     * Метод возвращающий сумму транзакции, введенную клиентом.
     * Также проверяет её на корректность ввода.
     * @param reader - объект для чтения данных из консоли.
     * @return сумму транзакции не менее 1 копейки.
     */
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

    /**
     * Метод возвращающий счёт клиента для перевода по имени.
     * Также проверяет введенные данные на корректность заполнения, наличие такого счёта в БД,
     * а также не является ли счёт для перевода счетом с которого идёт перечисление средств.
     * @see Account
     * @param reader - объект чтения данных из консоли.
     * @param userAccount - счёт клиента с которого он переводит деньги.
     * @return счёт для перевода.
     */
    public Account readAccountByName(BufferedReader reader, Account userAccount) {
        Account account = new Account();
        while (account.getId() == 0) {
            String accountName;
            while (true) {
                try {
                    if (isCorrectAccountName(accountName = reader.readLine().toUpperCase())) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(INCORRECT_ACCOUNT_FORMAT);
            }
            account = accountService.getAccountByName(accountName);
            if (account.getId() == 0) {
                System.out.println(INCORRECT_ACCOUNT_FROM_DB);
            }
            if (account.getId() == userAccount.getId()) {
                System.out.println(INCORRECT_ACCOUNT_YOURSELF);
                account.setId(0);
            }
        }
        return account;
    }

    /**
     * Метод для проверки корректности имени счёта.
     * @param answer - строка с именем счёта.
     * @return true если счёт введен корректно.
     */
    public static boolean isCorrectAccountName(String answer) {
        String[] strings = answer.split(" ");
        boolean ans = false;
        if (strings.length == 7) {
            for (String str : strings) {
                ans = str.matches("[A-Z0-9]{4}");
                if (!ans) {
                    break;
                }
            }
        }
        return ans;
    }

    /**
     * Метод для вычисления количества цифр после запятой в строке преобразованной в число.
     * @param answer строка с числом.
     * @return число знаков после запятой либо 3 если их много.
     */
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

    /**
     * Метод для возвращения подтверждения клиентом продолжения работы.
     * @param reader - объект для чтения данных из консоли.
     * @return true если да.
     */
    public boolean isContinue(BufferedReader reader) {
        String answer;
        try {
            answer = reader.readLine().toUpperCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return answer.equals("ДА") || answer.equals("YES");
    }

    /**
     * Метод для получения ответа клиента в меню состоящем из 2х пунктов
     * @param reader - объект для чтения данных из консоли.
     * @return ответ клиента.
     */
    public String returnAnswerFromMenuTwoPoints(BufferedReader reader) {
        String answer = null;
        while (answer == null) {
            try {
                answer = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch (answer) {
                case ("1") -> {
                    return "1";
                }
                case ("2") -> {
                    return "2";
                }
                default -> {
                    System.out.println(INCORRECT_CHOICE_FROM_MENU);
                    answer = null;
                }
            }
        }
        return answer;
    }
    /**
     * Метод для получения ответа клиента в меню состоящем из 3х пунктов
     * @param reader - объект для чтения данных из консоли.
     * @return ответ клиента.
     */
    public String returnAnswerFromMenuThreePoints(BufferedReader reader) {
        String answer = null;
        while (answer == null) {
            try {
                answer = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch (answer) {
                case ("1") -> {
                    return "1";
                }
                case ("2") -> {
                    return "2";
                }
                case ("3") -> {
                    return "3";
                }
                default -> {
                    System.out.println(INCORRECT_CHOICE_FROM_MENU);
                    answer = null;
                }
            }
        }
        return answer;
    }

    /**
     * Метод для формирования отчёта клиенту за период.
     * Период по выбору клиента - месяц, год либо весь период.
     * @param reader - объект для чтения данных из консоли.
     * @param userAccount - объект клиента
     */
    public void accountStatementOutput(BufferedReader reader, Account userAccount) {
        LocalDateTime fromDate;
        LocalDateTime toDate;
        System.out.print(CHOOSE_PERIOD);
        switch (returnAnswerFromMenuThreePoints(reader)) {
            case ("1") -> {
                int year = selectionYear(reader);
                int month = selectionMonth(reader);
                fromDate = LocalDateTime.of(year, month, 1, 0, 0, 0, 0);
                if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    toDate = LocalDateTime.of(year, month, 31, 23, 59, 59, 999_999_000);
                } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                    toDate = LocalDateTime.of(year, month, 30, 23, 59, 59, 999_999_000);
                } else {
                    if (year % 4 == 0) {
                        toDate = LocalDateTime.of(year, month, 29, 23, 59, 59, 999_999_000);
                    } else {
                        toDate = LocalDateTime.of(year, month, 28, 23, 59, 59, 999_999_000);
                    }
                }
                List<Transaction> transactionsByUserIdAndPeriod =
                        transactionService.getTransactionsByUserIdAndPeriod(userAccount.getId(), fromDate, toDate);
                accountStatementGenerator.saveAccountStatement(transactionsByUserIdAndPeriod, userAccount, fromDate, toDate, 0, 0);
            }
            case ("2") -> {
                int year = selectionYear(reader);
                fromDate = LocalDateTime.of(year, 1, 1, 0, 0, 0, 0);
                toDate = LocalDateTime.of(year, 12, 31, 0, 0, 0, 999_999_000);
                List<Transaction> transactionsByUserIdAndPeriod =
                        transactionService.getTransactionsByUserIdAndPeriod(userAccount.getId(), fromDate, toDate);
                accountStatementGenerator.saveAccountStatement(transactionsByUserIdAndPeriod, userAccount, fromDate, toDate, 0, 0);
            }
            case ("3") -> {
                fromDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
                toDate = LocalDateTime.now();
                List<Transaction> transactionsByUserIdAndPeriod =
                        transactionService.getTransactionsByUserIdAndPeriod(userAccount.getId(), fromDate, toDate);
                accountStatementGenerator.saveAccountStatement(transactionsByUserIdAndPeriod, userAccount, fromDate, toDate, 0, 0);
            }
        }

    }

    /**
     * Метод по выбору месяца клиентом.
     * @param reader - объект для чтения данных из консоли.
     * @return номер месяца, выбранного клиентом.
     */
    private int selectionMonth(BufferedReader reader) {
        System.out.print(CHOOSE_MONTH);
        int month = 0;
        while (month < 1 || month > 12) {
            try {
                month = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (month < 1 || month > 12) {
                System.out.println(INCORRECT_CHOICE_FROM_MENU);
            }
        }
        return month;
    }
    /**
     * Метод по выбору года клиентом.
     * @param reader - объект для чтения данных из консоли.
     * @return номер года, выбранного клиентом.
     */
    private int selectionYear(BufferedReader reader) {
        int currentYear = LocalDateTime.now().getYear();
        System.out.printf(CHOOSE_YEAR, currentYear);
        int year = 0;
        while (year < 1970 || year > currentYear) {
            try {
                year = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (year < 1970 || year > currentYear) {
                System.out.println(INCORRECT_CHOICE_FROM_MENU);
            }
        }
        return year;
    }
}
