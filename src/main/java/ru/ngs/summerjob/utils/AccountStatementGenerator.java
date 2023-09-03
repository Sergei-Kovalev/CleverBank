package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.entity.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Утилитный класс для формирования и сохранения выписки по счету за период (месяц, год, весь период).
 * Является реализацией абстрактного класса:
 * @see AbstractStatementGenerator
 */
public class AccountStatementGenerator extends AbstractStatementGenerator {
    /**
     * Поле для шапки отчёта.
     */
    private final static String HEAD = "                            Выписка";
    /**
     * Поле для концовки отчёта.
     */
    private final static String END_OF_STATEMENT = """
            Дата   |         Примечание                  | Сумма
            ----------------------------------------------------------
            """;

    /**
     * Метод для сохранения сформированного отчёта в корне проекта.
     * @param accountStatement - сформированный отчёт в String поле.
     * @param fromDate - дата начала формирования отчёта.
     * @param toDate - дата окончания формированя отчёта.
     */
    @Override
    public void accountStatementOutputInFile(String accountStatement, LocalDateTime fromDate, LocalDateTime toDate) {
        String fileName = "Account statement" + fillDate(fromDate) + "-" + fillDate(toDate);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
            writer.write(accountStatement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращающий шапку отчёта
     * @return возвращает строку из константы HEAD.
     */
    public String fillHead() {
        return HEAD;
    }

    /**
     * Метод возвращающий нижние колонки с заполненными транзакциями.
     * @param transactions - принимает список всех транзакций для отображения.
     * @param totalIncome - в данной реализации не используется.
     * @param totalOutcome - в данной реализации не используется.
     * @return возвращает строку с заполненными полями транзакций.
     */
    @Override
    public String fillTransactionsColumns(List<Transaction> transactions, double totalIncome, double totalOutcome) {
        StringBuilder builder = new StringBuilder();

        builder.append(END_OF_STATEMENT);
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
}
