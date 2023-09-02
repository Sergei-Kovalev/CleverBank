package ru.ngs.summerjob.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Transaction;
import ru.ngs.summerjob.entity.TransactionType;
import ru.ngs.summerjob.service.*;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "transaction", urlPatterns = {"/transaction"})
public class TransactionServlet extends HttpServlet {
    TransactionService transactionService;
    AccountService accountService;
    TransactionTypeService transactionTypeService;

    @Override
    public void init(ServletConfig config) {
        transactionService = new TransactionServiceImpl();
        accountService = new AccountServiceImpl();
        transactionTypeService = new TransactionTypeServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idFromReq = req.getParameter("id");
        long id = Long.parseLong(idFromReq);
        Transaction transaction = transactionService.getTransactionById(id);
        resp.getWriter().println(transaction);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        TransactionType transactionType = transactionTypeService.getTransactionTypeById(Long.parseLong(req.getParameter("type")));
        if (transactionType.getId() == 0) {
            resp.getWriter().println("Вы ввели тип транзакций, которого не существует");
        } else {
            transaction.setType(transactionType);
        }

        Account accountSender = accountService.getAccountById(Long.parseLong(req.getParameter("sender")));
        transaction.setAccountSender(accountSender);
        Account accountRecipient = accountService.getAccountById(Long.parseLong(req.getParameter("recipient")));
        if (accountSender.getId() == 0) {
            resp.getWriter().println("Вы ввели id получателя, которого не существует");
        } else {
            transaction.setAccountRecipient(accountRecipient);
        }
        double amount = Double.parseDouble(req.getParameter("amount"));
        if (amount <= 0) {
            resp.getWriter().println("Введенная сумма не может быть меньше 1 копейки");
        }
        transaction.setAmount(amount);

        Transaction transactionAfterSaving = transactionService.saveTransactionForServlet(transaction);

        resp.getWriter().println(transactionAfterSaving);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Transaction transaction = new Transaction();
        transaction.setId(Long.parseLong(req.getParameter("id")));
        transaction.setAmount(Double.parseDouble(req.getParameter("amount")));
        Transaction transactionAfterUpdate = transactionService.updateTransaction(transaction);
        resp.getWriter().println(transactionAfterUpdate);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = transactionService.deleteTransactionById(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
