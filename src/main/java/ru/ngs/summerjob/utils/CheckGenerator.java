package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.dao.CheckDao;
import ru.ngs.summerjob.dao.CheckDaoImpl;
import ru.ngs.summerjob.entity.Check;
import ru.ngs.summerjob.entity.Transaction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckGenerator {
    CheckDao checkDao;

    public CheckGenerator() {
        this.checkDao = new CheckDaoImpl();
    }

    private final static String CHECK_TEMPLATE = """
            -----------------------------------------------------------
            |                   Банковский чек                        |
            | %s                                     %s |
            | Тип транзакции:      %s |
            | Банк отправителя:    %s |
            | Банк получателя:     %s |
            | Счет отправителя:    %s |
            | Счет получателя:     %s |
            | Сумма:               %s |
            |_________________________________________________________|
            """;

    private String generateCheck(Transaction transaction) {
        String bankSender;
        String accountSender;
        double amount;

        if (transaction.getAccountSender() == null) {
            bankSender = null;
            accountSender = null;
        } else {
            bankSender = transaction.getAccountSender().getBank().getName();
            accountSender = transaction.getAccountSender().getName();
        }
        if (transaction.getType().getId() == 2) {
            amount = -transaction.getAmount();
        } else {
            amount = transaction.getAmount();
        }
        String amountString = String.format("%.2f", amount);
        return String.format(CHECK_TEMPLATE,
                fillDate(transaction.getDate()),
                fillTime(transaction.getDate()),
                fillLeftSpaces(transaction.getType().getName()),
                fillLeftSpaces(bankSender),
                fillLeftSpaces(transaction.getAccountRecipient().getBank().getName()),
                fillLeftSpaces(accountSender),
                fillLeftSpaces(transaction.getAccountRecipient().getName()),
                fillLeftSpaces(amountString));
    }
    public void saveCheckInFile(Transaction transaction) {
        long checkNumber = checkDao.saveCheck(new Check());
        String check = generateCheck(transaction);
        checkOutputInFile(check, checkNumber);
    }

    private String fillLeftSpaces(String string) {
        if (string == null) {
            string = "-----";
        }
        int numberOfCharacters = 34;
        return String.format("%" + numberOfCharacters + "s", string);
    }

    private String fillTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return date.format(formatter);
    }

    private String fillDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
        return date.format(formatter);
    }

    private void checkOutputInFile(String check, long checkNumber) {
        File filePath = new File("check");
        filePath.mkdir();
        String fileName = "Check" + checkNumber + ".txt";
        File checkFile = new File(filePath + "\\" + fileName);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(checkFile))) {
            writer.write(check);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
