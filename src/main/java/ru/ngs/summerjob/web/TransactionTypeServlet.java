package ru.ngs.summerjob.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ngs.summerjob.entity.TransactionType;
import ru.ngs.summerjob.service.TransactionTypeService;
import ru.ngs.summerjob.service.TransactionTypeServiceImpl;

import java.io.IOException;
/**
 * @author Sergey Kovalev
 * Класс сервлета для работы с веб.
 * Реализует возможности CRUD операций с сущностью
 * @see TransactionType
 */
@WebServlet(name = "transactionType", urlPatterns = {"/transactionType"})
public class TransactionTypeServlet extends HttpServlet {
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
        transactionTypeService = new TransactionTypeServiceImpl();
    }
    /**
     * Реализация метода GET / Read.
     * Принимает из запроса Value: id = id типа транзакций
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
        TransactionType transactionTypeById = transactionTypeService.getTransactionTypeById(id);
        resp.getWriter().println(transactionTypeById);
    }
    /**
     * Реализация метода POST / Create.
     * Принимает из запроса Value:  name = имя типа транзакции.
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TransactionType transactionType = new TransactionType();
        transactionType.setName(req.getParameter("name"));
        TransactionType transactionTypeAfterSaving = transactionTypeService.saveTransactionType(transactionType);
        resp.getWriter().println(transactionTypeAfterSaving);
    }
    /**
     * Реализация метода PUT / Update.
     * Принимает из запроса Value:  id = id типа транзакции, (параметр для поиска поля для изменения в БД)
     *                              name = имя типа транзакции, (изменяемый параметр)
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TransactionType transactionType = new TransactionType();
        transactionType.setId(Long.parseLong(req.getParameter("id")));
        transactionType.setName(req.getParameter("name"));
        TransactionType transactionTypeAfterUpdate = transactionTypeService.updateTransactionType(transactionType);
        resp.getWriter().println(transactionTypeAfterUpdate);
    }
    /**
     * Реализация метода DELETE.
     * Принимает из запроса Value: id = id типа транзакции для удаления записи о нём из БД.
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = transactionTypeService.deleteTransactionTypeById(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
