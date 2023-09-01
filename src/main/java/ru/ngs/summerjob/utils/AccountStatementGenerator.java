package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccountStatementGenerator {
    private final static String ACCOUNT_STATEMENT_TEMPLATE = """
                                    Выписка
                                  Clever-Bank
            Клиент                      | %s
            Счет                        | %s
            Валюта                      | %s
            Дата открытия               | %s
            Период                      | %s - %s
            Дата и время формирования   | %s
            Остаток                     | %s %s
                Дата   |         Примечание                  | Сумма
            ----------------------------------------------------------
            %s
            """;
    public String generateAccountStatement(List<Transaction> transactions, Account userAccount, LocalDateTime fromDate, LocalDateTime toDate) {

        return String.format(ACCOUNT_STATEMENT_TEMPLATE,
                userAccount.getUser().getName(),
                userAccount.getName(),
                userAccount.getCurrency().getName(),
                fillDate(userAccount.getOpeningDate()),
                fillDate(fromDate),
                fillDate(toDate),
                fillDateAndTime(LocalDateTime.now()),
                String.format("%.2f",userAccount.getBalance()),
                userAccount.getCurrency().getName(),
                fillTransactionsColumns(transactions)
                );
    }
    public void saveAccountStatement(List<Transaction> transactions, Account userAccount, LocalDateTime fromDate, LocalDateTime toDate) {
        String accountStatement = generateAccountStatement(transactions, userAccount, fromDate, toDate);
        accountStatementOutputInFile(accountStatement, fromDate, toDate);
    }

    private void accountStatementOutputInFile(String accountStatement, LocalDateTime fromDate, LocalDateTime toDate) {
        String fileName = "Account statement" + fillDate(fromDate) + "-" + fillDate(toDate);
//        Document document = new Document();
//        PdfWriter.getInstance(document, new FileOutputStream(fileName + ".pdf"));
//        Font font = FontFactory.getFont("C:\\Windows\\Fonts\\Arial.ttf", BaseFont.IDENTITY_H, 10);
//        document.open();
//        Paragraph paragraph = new Paragraph(accountStatement, font);
//        document.add(paragraph);
//        document.close();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
            writer.write(accountStatement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String fillTransactionsColumns(List<Transaction> transactions) {
        StringBuilder builder = new StringBuilder();
        for (Transaction transaction : transactions) {
            String stringFromTransaction;
            if (transaction.getAccountSender().getId() == 0) {
                stringFromTransaction = String.format("%s | %s | %s",
                        fillDate(transaction.getDate()),
                        fillRightSpaces(transaction.getType().getName(), null),
                        String.format("%.2f",transaction.getAmount()));
            } else {
                stringFromTransaction = String.format("%s | %s | %s",
                        fillDate(transaction.getDate()),
                        fillRightSpaces(transaction.getType().getName(), transaction.getAccountSender().getUser().getName()),
                        String.format("%.2f",transaction.getAmount()));
            }
            builder.append(stringFromTransaction).append(System.lineSeparator());
        }
        return builder.toString();
    }

    private Object fillDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy");
        return date.format(formatter);
    }
    private Object fillDateAndTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy, hh.mm");
        return date.format(formatter);
    }
    private String fillRightSpaces(String string1, String string2) {
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
}
