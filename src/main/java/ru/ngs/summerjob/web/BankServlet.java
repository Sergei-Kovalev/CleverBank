package ru.ngs.summerjob.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ngs.summerjob.entity.Bank;
import ru.ngs.summerjob.service.BankService;
import ru.ngs.summerjob.service.BankServiceImpl;

import java.io.IOException;

/**
 * @author Sergey Kovalev
 * Класс сервлета для работы с веб.
 * Реализует возможности CRUD операций с сущностью
 * @see Bank
 */
@WebServlet(name = "bank", urlPatterns = "/bank")
public class BankServlet extends HttpServlet {
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see BankService
     */
    BankService bankService;
    /**
     * Загружает сервисы необходимые для работы сервлета при инициализации
     */
    @Override
    public void init(ServletConfig config) {
        bankService = new BankServiceImpl();
    }
    /**
     * Реализация метода GET / Read.
     * Принимает из запроса Value: id = id банка.
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
        Bank bank = bankService.getBankById(id);
        resp.getWriter().println(bank);
    }
    /**
     * Реализация метода POST / Create.
     * Принимает из запроса Value:  name = имя банка.
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Bank bank = new Bank();
        bank.setName(req.getParameter("name"));
        Bank bankAfterSaving = bankService.saveBank(bank);
        resp.getWriter().println(bankAfterSaving);
    }
    /**
     * Реализация метода PUT / Update.
     * Принимает из запроса Value:  id = id банка, (параметр для поиска поля для изменения в БД)
     *                              name = наименование банка, (изменяемый параметр).
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Bank bank = new Bank();
        bank.setId(Long.parseLong(req.getParameter("id")));
        bank.setName(req.getParameter("name"));
        Bank bankAfterUpdate = bankService.updateBank(bank);
        resp.getWriter().println(bankAfterUpdate);
    }
    /**
     * Реализация метода DELETE.
     * Принимает из запроса Value: id = id банка для удаления записи о нём из БД.
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = bankService.deleteBank(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
