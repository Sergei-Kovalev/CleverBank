package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Абстрактный класс для формирования и сохранения отчётов.
 * В данном приложении его реализациями являются:
 * @see StatementMoneyGenerator
 * @see AccountStatementGenerator
 */
public abstract class AbstractStatementGenerator {
    /**
     * Поле с константой макета общего для всех наследников класса
     */
    private final static String ACCOUNT_STATEMENT_TEMPLATE = """
            %s
                                      Clever-Bank
            Клиент                      | %s
            Счет                        | %s
            Валюта                      | %s
            Дата открытия               | %s
            Период                      | %s - %s
            Дата и время формирования   | %s
            Остаток                     | %s %s
            %s
            """;

    /**
     * Метод общий для всех наследников класса.
     * Предназначен для генерации отчёта по шаблону.
     * @param transactions - список транзакций для отображения (если необходимы)
     * @param userAccount - счет по которому формируется отчёт.
     * @param fromDate - дата начала формирования отчёта.
     * @param toDate - дата окончания формирования отчёта.
     * @param totalIncome - общая сумма пришедших на счет денежных средств.
     * @param totalOutcome - общая сумма расхода денежных средств по счёту.
     * @return строчное представление отчёта.
     */
    private String generateAccountStatement(List<Transaction> transactions, Account userAccount, LocalDateTime fromDate, LocalDateTime toDate, double totalIncome, double totalOutcome) {

        return String.format(ACCOUNT_STATEMENT_TEMPLATE,
                fillHead(),
                userAccount.getUser().getName(),
                userAccount.getName(),
                userAccount.getCurrency().getName(),
                fillDate(userAccount.getOpeningDate()),
                fillDate(fromDate),
                fillDate(toDate),
                fillDateAndTime(LocalDateTime.now()),
                String.format("%.2f",userAccount.getBalance()),
                userAccount.getCurrency().getName(),
                fillTransactionsColumns(transactions, totalIncome, totalOutcome)
        );
    }

    /**
     * Абстрактный метод, заполняющий шапку отчёта.
     * Реализация зависит от класса наследника.
     * @return строку с заполненной шапкой
     */
    public abstract String fillHead();

    /**
     * Метод общий для всех наследников класса.
     * Предназначен для получения отчёта в формате String и сохранения.
     * @param transactions - список транзакций для отображения (если необходимы)
     * @param userAccount - счет по которому формируется отчёт.
     * @param fromDate - дата начала формирования отчёта.
     * @param toDate - дата окончания формирования отчёта.
     * @param totalIncome - общая сумма пришедших на счет денежных средств.
     * @param totalOutcome - общая сумма расхода денежных средств по счёту.
     */
    public void saveAccountStatement(List<Transaction> transactions, Account userAccount, LocalDateTime fromDate, LocalDateTime toDate, double totalIncome, double totalOutcome) {
        String accountStatement = generateAccountStatement(transactions, userAccount, fromDate, toDate, totalIncome, totalOutcome);
        accountStatementOutputInFile(accountStatement, fromDate, toDate);
    }

    /**
     * Абстрактный метод, для вывода сформированного отчёта в файл.
     * Реализация зависит от класса наследника.
     * @param accountStatement - отчёт в виде строки.
     * @param fromDate - дана начала формирования отчёта.
     * @param toDate - дата окончания формирования отчёта.
     */
    public abstract void accountStatementOutputInFile(String accountStatement, LocalDateTime fromDate, LocalDateTime toDate);

    /**
     * Абстрактный метод, для заполнения колонок транзакций либо другой бизнес отчётности.
     * Реализация зависит от класса наследника.
     * @param transactions - список транзакций для отображения.
     * @param totalIncome - общая сумма поступлений на счёт.
     * @param totalOutcome - общая сумма расходов по счёту.
     * @return - возвращает строку с заполненными данными для бизнес отчётности.
     */
    public abstract String fillTransactionsColumns(List<Transaction> transactions, double totalIncome, double totalOutcome);

    /**
     * Метод общий для всех наследников класса.
     * Форматирует дату в строку по заданному шаблону.
     * @param date - принимает дату в формате LocalDateTime.
     * @return - строковое представление даты.
     */
    public String fillDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy");
        return date.format(formatter);
    }

    /**
     * Метод общий для всех наследников класса.
     * Форматирует дату и время в строку по заданному шаблону.
     * @param date - принимает дату в формате LocalDateTime.
     * @return - строковое представление даты и времени.
     */
    private String fillDateAndTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy, hh.mm");
        return date.format(formatter);
    }

    /**
     * Метод общий для всех наследников класса.
     * Формирует строку из двух строк с необходимым количеством пробелов.
     * @param string1 - строка для форматирования.
     * @param string2 - строка для форматирования.
     * @return - строка форматированная по шаблону.
     */
    public String fillRightSpaces(String string1, String string2) {
        String stringToFormat;
        if (string2 == null) {
            stringToFormat = string1;
        } else {
            String[] strings = string2.split(" ");
            stringToFormat = string1 + " от " + strings[0];
        }
        int numberOfCharacters = 35;
        return String.format("%-" + numberOfCharacters + "s", stringToFormat);
    }

    /**
     * Метод общий для всех наследников класса.
     * Заполняет строку необходимым количеством пробелов слева.
     * @param string - входящая строка.
     * @return - строку с нужным количеством пробелов.
     */
    public String fillLeftSpaces(String string) {
        if (string == null) {
            string = "-----";
        }
        int numberOfCharacters = 22;
        return String.format("%" + numberOfCharacters + "s", string);
    }
}
