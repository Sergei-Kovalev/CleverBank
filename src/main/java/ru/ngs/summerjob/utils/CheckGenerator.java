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

/**
 * @author Sergey Kovalev
 * Класс реализующий формирование чека по каждой операции,
 * а также сохранение чека в папке check.
 */
public class CheckGenerator {
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see CheckDao
     */
    CheckDao checkDao;
    /**
     * Конструктор, загружает необходимые имплементации сервисов.
     */
    public CheckGenerator() {
        this.checkDao = new CheckDaoImpl();
    }
    /**
     * Константа для макета чека.
     */
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

    /**
     * Метод для формирования чека.
     * @param transaction - принимает транзакцию для формирования по ней чека.
     * @return возвращает чек в String формате.
     */
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

    /**
     * Главный метод для сохранения чека.
     * @param transaction - принимает транзакцию для сохранения чека.
     */
    public void saveCheckInFile(Transaction transaction) {
        long checkNumber = checkDao.saveCheck(new Check());
        String check = generateCheck(transaction);
        checkOutputInFile(check, checkNumber);
    }

    /**
     * Вспомогательный метод для заполнения слева пробелами для корректного отображения в чеке.
     * @param string - принимает строку со значением для заполнения в поле.
     * @return - возвращает строку с заполненными пробелами. Если поле пустое возвращает строку с "-----"
     */
    private String fillLeftSpaces(String string) {
        if (string == null) {
            string = "-----";
        }
        int numberOfCharacters = 34;
        return String.format("%" + numberOfCharacters + "s", string);
    }

    /**
     * Метод преобразующий время из LocalDateTime в строку по формату.
     * @param date - принимает дату в формате LocalDateTime.
     * @return - возвращает строку с временем по формату.
     */
    private String fillTime(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return date.format(formatter);
    }

    /**
     * Метод преобразующий дату из LocalDateTime в строку по формату.
     * @param date - принимает дату в формате LocalDateTime.
     * @return - возвращает строку с датой по формату.
     */
    private String fillDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-LL-yyyy");
        return date.format(formatter);
    }

    /**
     * Метод сохраняющий сформированный чек в папку проекта /chek
     * @param check - Строка со сформированным чеком.
     * @param checkNumber - номер чека (берется из следующего свободного в БД).
     */
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
