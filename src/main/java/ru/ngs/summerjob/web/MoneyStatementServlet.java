package ru.ngs.summerjob.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.service.AccountService;
import ru.ngs.summerjob.service.AccountServiceImpl;
import ru.ngs.summerjob.service.TransactionService;
import ru.ngs.summerjob.service.TransactionServiceImpl;
import ru.ngs.summerjob.utils.AbstractStatementGenerator;
import ru.ngs.summerjob.utils.StatementMoneyGenerator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet(name = "moneyStatement", urlPatterns = {"/moneyStatement"})
public class MoneyStatementServlet extends HttpServlet {
    TransactionService transactionService;
    AccountService accountService;

    @Override
    public void init(ServletConfig config) {
        transactionService = new TransactionServiceImpl();
        accountService = new AccountServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account account = new Account();
        String accountName = req.getParameter("accountName");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-L-dd HH:mm:ss.SSS");
        String fromDateString = req.getParameter("fromDate");
        String toDateString = req.getParameter("toDate");
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;
        try {
            fromDate = LocalDateTime.parse(fromDateString + " 00:00:00.000", dateTimeFormatter);
        } catch (DateTimeParseException e) {
            resp.getWriter().println("Проверьте дату. Пример формата: 2023-12-31");
        }
        try {
            toDate = LocalDateTime.parse(toDateString + " 00:00:00.000", dateTimeFormatter);
        } catch (DateTimeParseException e) {
            resp.getWriter().println("Проверьте дату. Пример формата: 2023-12-31");
        }
        if (!isCorrectAccountName(accountName)){
            resp.getWriter().println("Проверьте номер счета. Формат: XXXX XXXX XXXX XXXX XXXX XXXX XXXX");
        } else {
            account = accountService.getAccountByName(accountName);
        }
        double totalIncome = transactionService.getTotalIncome(account, fromDate, toDate);
        double totalOutcome = transactionService.getTotalOutcome(account, fromDate, toDate);

        AbstractStatementGenerator generator= new StatementMoneyGenerator();
        generator.saveAccountStatement(null, account, fromDate, toDate,totalIncome, totalOutcome);
        resp.getWriter().println("Ваш отчёт готов проверьте папку: apache-tomcat-10.1.13\\bin\\statement-money");
    }

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
}
