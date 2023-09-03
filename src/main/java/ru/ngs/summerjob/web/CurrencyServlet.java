package ru.ngs.summerjob.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ngs.summerjob.entity.Currency;
import ru.ngs.summerjob.service.CurrencyService;
import ru.ngs.summerjob.service.CurrencyServiceImpl;

import java.io.IOException;

/**
 * @author Sergey Kovalev
 * Класс сервлета для работы с веб.
 * Реализует возможности CRUD операций с сущностью
 * @see Currency
 */
@WebServlet(name = "currency", urlPatterns = {"/currency"})
public class CurrencyServlet extends HttpServlet {
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see CurrencyService
     */
    CurrencyService currencyService;
    /**
     * Загружает сервисы необходимые для работы сервлета при инициализации
     */
    @Override
    public void init(ServletConfig config) {
        currencyService = new CurrencyServiceImpl();
    }
    /**
     * Реализация метода GET / Read.
     * Принимает из запроса Value: id = id валюты.
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
        Currency currency = currencyService.getCurrencyById(id);
        resp.getWriter().println(currency);
    }
    /**
     * Реализация метода POST / Create.
     * Принимает из запроса Value:  name = имя валюты.
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Currency currency = new Currency();
        currency.setName(req.getParameter("name"));
        Currency currencyAfterSaving = currencyService.saveCurrency(currency);
        resp.getWriter().println(currencyAfterSaving);
    }
    /**
     * Реализация метода PUT / Update.
     * Принимает из запроса Value:  id = id валюты, (параметр для поиска поля для изменения в БД)
     *                              name = имя пользователя, (изменяемый параметр).
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Currency currency = new Currency();
        currency.setId(Long.parseLong(req.getParameter("id")));
        currency.setName(req.getParameter("name"));
        Currency currencyAfterUpdate = currencyService.updateCurrency(currency);
        resp.getWriter().println(currencyAfterUpdate);
    }
    /**
     * Реализация метода DELETE.
     * Принимает из запроса Value: id = id валюты для удаления записи о ней из БД.
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = currencyService.deleteCurrency(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
