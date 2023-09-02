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

@WebServlet(name = "bank", urlPatterns = "/bank")
public class BankServlet extends HttpServlet {
    BankService bankService;

    @Override
    public void init(ServletConfig config) {
        bankService = new BankServiceImpl();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idFromReq = req.getParameter("id");
        long id = Long.parseLong(idFromReq);
        Bank bank = bankService.getBankById(id);
        resp.getWriter().println(bank);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Bank bank = new Bank();
        bank.setName(req.getParameter("name"));
        Bank bankAfterSaving = bankService.saveBank(bank);
        resp.getWriter().println(bankAfterSaving);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Bank bank = new Bank();
        bank.setId(Long.parseLong(req.getParameter("id")));
        bank.setName(req.getParameter("name"));
        Bank bankAfterUpdate = bankService.updateBank(bank);
        resp.getWriter().println(bankAfterUpdate);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = bankService.deleteBank(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
