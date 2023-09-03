package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractStatementGenerator {
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

    public abstract String fillHead();

    public void saveAccountStatement(List<Transaction> transactions, Account userAccount, LocalDateTime fromDate, LocalDateTime toDate, double totalIncome, double totalOutcome) {
        String accountStatement = generateAccountStatement(transactions, userAccount, fromDate, toDate, totalIncome, totalOutcome);
        accountStatementOutputInFile(accountStatement, fromDate, toDate);
    }

    public abstract void accountStatementOutputInFile(String accountStatement, LocalDateTime fromDate, LocalDateTime toDate);

    public abstract String fillTransactionsColumns(List<Transaction> transactions, double totalIncome, double totalOutcome);

    public Object fillDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy");
        return date.format(formatter);
    }
    private Object fillDateAndTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy, hh.mm");
        return date.format(formatter);
    }
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
    public String fillLeftSpaces(String string) {
        if (string == null) {
            string = "-----";
        }
        int numberOfCharacters = 22;
        return String.format("%" + numberOfCharacters + "s", string);
    }
}
