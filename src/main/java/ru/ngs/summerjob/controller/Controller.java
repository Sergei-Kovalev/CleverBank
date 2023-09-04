package ru.ngs.summerjob.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("метод getUserByLoginAndPassword принимает строку login=" + login + " и строку password=" + password);
        User userByLoginAndPassword = userService.getUserByLoginAndPassword(login, password);
        log.info("метод getUserByLoginAndPassword возвращает объект клиента:" + userByLoginAndPassword);
        return userByLoginAndPassword;
    }
    /**
     * Получает объект класса тип транзакции по id.
     * @see TransactionType
     * @param id - id клиента.
     * @return объект класса тип транзакции.
     */
    private TransactionType getTransactionTypeById(long id) {
        log.info("метод getTransactionTypeById принимает id транзакции = " + id);
        TransactionType transactionType = transactionTypeService.getTransactionTypeById(id);
        log.info("метод getTransactionTypeById возвращает объект типа транзакции = " + transactionType);
        return transactionType;
    }

    /**
     * Метод для авторизации клиента
     * @see User
     * Исполняется пока клиент не введёт верный логин и пароль либо откажется пробовать снова
     * @param reader - объект для чтения данных из консоли.
     * @return объект класса клиент.
     */
    public User userAuthorizationMethod(BufferedReader reader) {
        log.info("Метод userAuthorizationMethod принимает объект для чтения данных из консоли");
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
        log.info("Метод userAuthorizationMethod возвращает объект клиента = " + user);
        return user;
    }

    /**
     * Метод для выбора клиентом в меню возможных типов транзакций по счёту.
     * @see TransactionType
     * @param reader - объект для чтения данных из консоли.
     * @return тип транзакции, которую выбрал клиент
     */
    public TransactionType selectionOfTransactionType(BufferedReader reader) {
        log.info("Метод selectionOfTransactionType принимает объект для чтения данных из консоли");
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
        log.info("Метод selectionOfTransactionType возвращает объект типа транзакции = " + transactionType);
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
        log.info("Метод selectionOfAccount принимает объект для чтения данных из консоли а также id клиента = " + userId);
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
        log.info("Метод selectionOfAccount возвращает объект клиента = " + account);
        return account;
    }

    /**
     * Метод для создания меню из всех счетов клиента.
     * Может вернуть сообщение об отсутствии открытых счетов.
     * @param userId - id клиента
     * @return строку с меню сгенерированному из всех счетов клиента.
     */
    public String createAccountsMenu(long userId) {
        log.info("Метод createAccountsMenu принимает id клиента = " + userId);
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
        log.info("Метод createAccountsMenu возвращает сформированную строку меню для выбора счетов = " + menu);
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
        log.info("Метод replenishmentOfOwnAccount принимает объекты: счёт = " + userAccount
                + " тип транзакции = " + transactionType + " сумма транзакции = " + amount);
        Transaction transaction = new Transaction();
        fillConstantFieldsForTransaction(transactionType, amount, transaction);

        transaction.setAccountSender(null);
        transaction.setAccountRecipient(userAccount);
        checkGenerator.saveCheckInFile(transaction);

        boolean result = transactionService.saveTransaction(transaction);
        log.info("Метод replenishmentOfOwnAccount возвращает результат успешности пополнения счёта = " + result);
        return result;
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
        log.info("Метод withdrawFromOwnAccount принимает объекты: счёт = " + userAccount
                + " тип транзакции = " + transactionType + " сумма транзакции = " + amount);
        Transaction transaction = new Transaction();
        fillConstantFieldsForTransaction(transactionType, amount, transaction);

        transaction.setAccountSender(null);
        transaction.setAccountRecipient(userAccount);
        checkGenerator.saveCheckInFile(transaction);

        boolean result = transactionService.saveTransaction(transaction);
        log.info("Метод withdrawFromOwnAccount возвращает результат успешности снятия денег со счёта = " + result);
        return result;
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
        log.info("Метод remittance принимает объекты: счёт клиента = " + userAccount + " счёт получателя = "
                + recepientAccount + " тип транзакции = " + transactionType + " сумма перевода = " + amount);
        Transaction transaction = new Transaction();
        fillConstantFieldsForTransaction(transactionType, amount, transaction);

        transaction.setAccountSender(userAccount);
        transaction.setAccountRecipient(recepientAccount);
        checkGenerator.saveCheckInFile(transaction);
        boolean result = transactionService.saveTransaction(transaction);
        log.info("Метод remittance возвращает результат успешности осуществления перевода = " + result);
        return result;
    }

    /**
     * Метод для заполнения стандартных полей при генерации транзакции
     * @param transactionType - объект типа транзакции.
     * @param amount - сумма транзакции.
     * @param transaction - транзакция для заполнения.
     */
    private static void fillConstantFieldsForTransaction(TransactionType transactionType, double amount, Transaction transaction) {
        log.info("Метод fillConstantFieldsForTransaction принимает объекты: тип транзакции = " + transactionType
                + " сумма платежа = " + amount + " транзакция = " + transaction);
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
        log.info("Метод readAmount принимает объект для чтения данных из консоли");
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
        log.info("Метод readAmount возвращает количество средств для перевода = " + amount);
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
        log.info("Метод readAccountByName принимает объект чтения данных из консоли, а также объект счёта клиента = " + userAccount);
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
        log.info("Метод readAccountByName возвращает объект счёта для перевода = " + account);
        return account;
    }

    /**
     * Метод для проверки корректности имени счёта.
     * @param answer - строка с именем счёта.
     * @return true если счёт введен корректно.
     */
    public static boolean isCorrectAccountName(String answer) {
        log.info("Метод isCorrectAccountName принимает строку со счётом = " + answer);
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
        log.info("Метод isCorrectAccountName возвращает результат проверки введенного счёта на корректность = " + ans);
        return ans;
    }

    /**
     * Метод для вычисления количества цифр после запятой в строке преобразованной в число.
     * @param answer строка с числом.
     * @return число знаков после запятой либо 3 если их много.
     */
    private int calculateNumbersAfterDot(String answer) {
        log.info("Метод calculateNumbersAfterDot принимает строку с введенной суммой = " + answer);
        String[] strings = answer.split("\\.");
        if (strings.length > 2) {
            log.info("Метод calculateNumbersAfterDot возвращает значение = 3");
            return 3;
        } else if (strings.length > 1) {
            log.info("Метод calculateNumbersAfterDot возвращает значение = " + strings.length);
            return strings[1].length();
        } else {
            log.info("Метод calculateNumbersAfterDot возвращает значение = 0");
            return 0;
        }
    }

    /**
     * Метод для возвращения подтверждения клиентом продолжения работы.
     * @param reader - объект для чтения данных из консоли.
     * @return true если да.
     */
    public boolean isContinue(BufferedReader reader) {
        log.info("Метод isContinue принимает объект для чтения данных из консоли");
        String answer;
        try {
            answer = reader.readLine().toUpperCase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Метод isContinue возвращает значение желания клиента продолжить работу = "
                + (answer.equals("ДА") || answer.equals("YES")));
        return answer.equals("ДА") || answer.equals("YES");
    }

    /**
     * Метод для получения ответа клиента в меню состоящем из 2х пунктов
     * @param reader - объект для чтения данных из консоли.
     * @return ответ клиента.
     */
    public String returnAnswerFromMenuTwoPoints(BufferedReader reader) {
        log.info("Метод returnAnswerFromMenuTwoPoints принимает объект для чтения данных из консоли");
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
        log.info("Метод returnAnswerFromMenuTwoPoints возвращает ответ пользователя из меню = " + answer);
        return answer;
    }
    /**
     * Метод для получения ответа клиента в меню состоящем из 3х пунктов
     * @param reader - объект для чтения данных из консоли.
     * @return ответ клиента.
     */
    public String returnAnswerFromMenuThreePoints(BufferedReader reader) {
        log.info("Метод returnAnswerFromMenuThreePoints принимает объект для чтения данных из консоли");
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
        log.info("Метод returnAnswerFromMenuThreePoints возвращает ответ пользователя из меню = " + answer);
        return answer;
    }

    /**
     * Метод для формирования отчёта клиенту за период.
     * Период по выбору клиента - месяц, год либо весь период.
     * @param reader - объект для чтения данных из консоли.
     * @param userAccount - объект клиента
     */
    public void accountStatementOutput(BufferedReader reader, Account userAccount) {
        log.info("Метод accountStatementOutput принимает объект для чтения данных из консоли, а также объект счёта = " + userAccount);
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
                log.info("Метод accountStatementOutput сохраняет счёт по транзакции за месяц");
            }
            case ("2") -> {
                int year = selectionYear(reader);
                fromDate = LocalDateTime.of(year, 1, 1, 0, 0, 0, 0);
                toDate = LocalDateTime.of(year, 12, 31, 0, 0, 0, 999_999_000);
                List<Transaction> transactionsByUserIdAndPeriod =
                        transactionService.getTransactionsByUserIdAndPeriod(userAccount.getId(), fromDate, toDate);
                accountStatementGenerator.saveAccountStatement(transactionsByUserIdAndPeriod, userAccount, fromDate, toDate, 0, 0);
                log.info("Метод accountStatementOutput сохраняет счёт по транзакции за год");

            }
            case ("3") -> {
                fromDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
                toDate = LocalDateTime.now();
                List<Transaction> transactionsByUserIdAndPeriod =
                        transactionService.getTransactionsByUserIdAndPeriod(userAccount.getId(), fromDate, toDate);
                accountStatementGenerator.saveAccountStatement(transactionsByUserIdAndPeriod, userAccount, fromDate, toDate, 0, 0);
                log.info("Метод accountStatementOutput сохраняет счёт по транзакции за весь период");
            }
        }

    }

    /**
     * Метод по выбору месяца клиентом.
     * @param reader - объект для чтения данных из консоли.
     * @return номер месяца, выбранного клиентом.
     */
    private int selectionMonth(BufferedReader reader) {
        log.info("Метод selectionMonth принимает объект для чтения данных из консоли");
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
        log.info("Метод selectionMonth возвращает числовое значение месяца выбранного клиентом = " + month);
        return month;
    }
    /**
     * Метод по выбору года клиентом.
     * @param reader - объект для чтения данных из консоли.
     * @return номер года, выбранного клиентом.
     */
    private int selectionYear(BufferedReader reader) {
        log.info("Метод selectionYear принимает объект для чтения данных из консоли");
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
        log.info("Метод selectionYear возвращает значение года выбранного клиентом = " + year);
        return year;
    }
}
