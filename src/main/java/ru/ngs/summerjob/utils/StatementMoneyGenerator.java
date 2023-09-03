package ru.ngs.summerjob.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import ru.ngs.summerjob.entity.Transaction;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Утилитный класс для формирования и сохранения statemen-money.pdf.
 * Является реализацией абстрактного класса:
 * @see AbstractStatementGenerator
 */
public class StatementMoneyGenerator extends AbstractStatementGenerator {
    /**
     * Поле для шапки отчёта.
     */
    private final static String HEAD = "                        Money Statement";
    /**
     * Поле для концовки отчёта.
     */
    private final static String END_OF_STATEMENT = """
                                        Приход | Уход
                        -------------------------------------------
                        """;

    /**
     * Метод для сохранения сформированного отчёта в папке: /statement-money
     * Из за особенностей работы в Intillj Idea Community Edition сохранение производится
     * в папке Tomcat'а в директории /bin
     * @param accountStatement - сформированный отчёт в String поле.
     * @param fromDate - дата начала формирования отчёта.
     * @param toDate - дата окончания формированя отчёта.
     */
    @Override
    public void accountStatementOutputInFile(String accountStatement, LocalDateTime fromDate, LocalDateTime toDate) {
        String fileName = "Account statement" + fillDate(fromDate) + "-" + fillDate(toDate);

        File filePath = new File("statement-money");
        filePath.mkdir();
        File fileToDelete = new File(filePath + "\\" + fileName + ".pdf");
        boolean result = fileToDelete.delete();
        if (result) {
            System.out.println("Такой файл существует - удалить");
        }
        else {
            System.out.println("Не существует - всё ок.");
        }
        File file = new File(filePath + "\\" + fileName + ".pdf");

        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();

            PDFont font = PDType0Font.load(document, new File("C:\\Windows\\Fonts\\consola.ttf"));

            contentStream.setFont(font, 12);

            contentStream.newLineAtOffset(50, 700);

            String[] strings = accountStatement.split("\n");

            contentStream.setLeading(14.5f);

            for (String string : strings) {
                String replace = string.replace("\r", "");
                contentStream.showText(replace);
                contentStream.newLine();
            }

            contentStream.endText();
            contentStream.close();

            document.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод возвращающий шапку отчёта
     * @return возвращает строку из константы HEAD.
     */
    @Override
    public String fillHead() {
        return HEAD;
    }

    /**
     * Метод возвращающий нижние колонки.
     * @param transactions - в данной реализации не используется.
     * @param totalIncome - принимает общую сумму пополненных разными способами средсв.
     * @param totalOutcome - принимает общую сумму расходов по счету.
     * @return - возвращает строку с заполненными полями.
     */
    @Override
    public String fillTransactionsColumns(List<Transaction> transactions, double totalIncome, double totalOutcome) {
        StringBuilder builder = new StringBuilder();
        builder.append(END_OF_STATEMENT);
        builder.append(fillLeftSpaces(String.valueOf(String.format("%.2f", totalIncome))));
        builder.append(String.format(" | %.2f", totalOutcome));

        return builder.toString();
    }
}
