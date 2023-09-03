package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.entity.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AccountStatementGenerator extends AbstractStatementGenerator {
    private final static String HEAD = "                            Выписка";
    private final static String END_OF_STATEMENT = """
            Дата   |         Примечание                  | Сумма
            ----------------------------------------------------------
            """;

    public String fillHead() {
        return HEAD;
    }

    @Override
    public void accountStatementOutputInFile(String accountStatement, LocalDateTime fromDate, LocalDateTime toDate) {
        String fileName = "Account statement" + fillDate(fromDate) + "-" + fillDate(toDate);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".txt"))) {
            writer.write(accountStatement);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
