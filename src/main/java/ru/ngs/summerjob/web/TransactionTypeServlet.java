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

@WebServlet(name = "transactionType", urlPatterns = {"/transactionType"})
public class TransactionTypeServlet extends HttpServlet {
    TransactionTypeService transactionTypeService;
    @Override
    public void init(ServletConfig config) {
        transactionTypeService = new TransactionTypeServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idFromReq = req.getParameter("id");
        long id = Long.parseLong(idFromReq);
        TransactionType transactionTypeById = transactionTypeService.getTransactionTypeById(id);
        resp.getWriter().println(transactionTypeById);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TransactionType transactionType = new TransactionType();
        transactionType.setName(req.getParameter("name"));
        TransactionType transactionTypeAfterSaving = transactionTypeService.saveTransactionType(transactionType);
        resp.getWriter().println(transactionTypeAfterSaving);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TransactionType transactionType = new TransactionType();
        transactionType.setId(Long.parseLong(req.getParameter("id")));
        transactionType.setName(req.getParameter("name"));
        TransactionType transactionTypeAfterUpdate = transactionTypeService.updateTransactionType(transactionType);
        resp.getWriter().println(transactionTypeAfterUpdate);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = transactionTypeService.deleteTransactionTypeById(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }
}
