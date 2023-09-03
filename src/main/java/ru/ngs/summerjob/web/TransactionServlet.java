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
/**
 * @author Sergey Kovalev
 * Класс сервлета для работы с веб.
 * Реализует возможности CRUD операций с сущностью
 * @see Transaction
 */
@WebServlet(name = "transaction", urlPatterns = {"/transaction"})
public class TransactionServlet extends HttpServlet {
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see TransactionService
     */
    TransactionService transactionService;
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see AccountService
     */
    AccountService accountService;
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see TransactionTypeService
     */
    TransactionTypeService transactionTypeService;
    /**
     * Загружает сервисы необходимые для работы сервлета при инициализации
     */
    @Override
    public void init(ServletConfig config) {
        transactionService = new TransactionServiceImpl();
        accountService = new AccountServiceImpl();
        transactionTypeService = new TransactionTypeServiceImpl();
    }
    /**
     * Реализация метода GET / Read.
     * Принимает из запроса Value: id = id транзакции
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idFromReq = req.getParameter("id");
        long id = Long.parseLong(idFromReq);
        Transaction transaction = transactionService.getTransactionById(id);
        resp.getWriter().println(transaction);
    }
    /**
     * Реализация метода POST / Create.
     * Принимает из запроса Value:  type = id типа транзакции,
     *                              sender = id отправителя транзакции,
     *                              recipient = id получателя транзакции,
     *                              amount = сумма трнзакции.
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
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
    /**
     * Реализация метода PUT / Update.
     * Принимает из запроса Value:  id = id транзакции, (параметр для поиска поля для изменения в БД)
     *                              amount = сумма транзакции. (изменяемый параметр)
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Transaction transaction = new Transaction();
        transaction.setId(Long.parseLong(req.getParameter("id")));
        transaction.setAmount(Double.parseDouble(req.getParameter("amount")));
        Transaction transactionAfterUpdate = transactionService.updateTransaction(transaction);
        resp.getWriter().println(transactionAfterUpdate);
    }
    /**
     * Реализация метода DELETE.
     * Принимает из запроса Value: id = id транзакции для удаления записи о ней из БД.
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = transactionService.deleteTransactionById(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
