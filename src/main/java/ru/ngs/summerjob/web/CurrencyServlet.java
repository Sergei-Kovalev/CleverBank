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

@WebServlet(name = "currency", urlPatterns = {"/currency"})
public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService;

    @Override
    public void init(ServletConfig config) {
        currencyService = new CurrencyServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idFromReq = req.getParameter("id");
        long id = Long.parseLong(idFromReq);
        Currency currency = currencyService.getCurrencyById(id);
        resp.getWriter().println(currency);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Currency currency = new Currency();
        currency.setName(req.getParameter("name"));
        Currency currencyAfterSaving = currencyService.saveCurrency(currency);
        resp.getWriter().println(currencyAfterSaving);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Currency currency = new Currency();
        currency.setId(Long.parseLong(req.getParameter("id")));
        currency.setName(req.getParameter("name"));
        Currency currencyAfterUpdate = currencyService.updateCurrency(currency);
        resp.getWriter().println(currencyAfterUpdate);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = currencyService.deleteCurrency(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
