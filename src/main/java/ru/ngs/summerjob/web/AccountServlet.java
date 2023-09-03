package ru.ngs.summerjob.web;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Bank;
import ru.ngs.summerjob.entity.Currency;
import ru.ngs.summerjob.entity.User;
import ru.ngs.summerjob.service.*;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Sergey Kovalev
 * Класс сервлета для работы с веб.
 * Реализует возможности CRUD операций с сущностью
 * @see Account
 */
@WebServlet(name = "account", urlPatterns = {"/account"})
public class AccountServlet extends HttpServlet {
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see AccountService
     */
    AccountService accountService;
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see BankService
     */
    BankService bankService;
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see UserService
     */
    UserService userService;
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
        accountService = new AccountServiceImpl();
        bankService = new BankServiceImpl();
        userService = new UserServiceImpl();
        currencyService = new CurrencyServiceImpl();
    }
    /**
     * Реализация метода GET / Read.
     * Принимает из запроса Value: id = id банковского счета.
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
        Account account = accountService.getAccountById(id);
        resp.getWriter().println(account);
    }
    /**
     * Реализация метода POST / Create.
     * Принимает из запроса Value:  name = номер банковского счета,
     *                              balance = первоначальная сумма на банковском счете,
     *                              user = id владельца счета,
     *                              bank = id банка для открытия счета,
     *                              currency = id валюты счета.
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account account = new Account();
        String name = req.getParameter("name");
        LocalDateTime openingDate = LocalDateTime.now();
        double balance = Double.parseDouble(req.getParameter("balance"));
        User user = userService.getUserById(Long.parseLong(req.getParameter("user")));
        Bank bank = bankService.getBankById(Long.parseLong(req.getParameter("bank")));
        Currency currency = currencyService.getCurrencyById(Long.parseLong(req.getParameter("currency")));
        if (!isCorrectAccountName(name)){
            resp.getWriter().println("Проверьте номер счета. Формат: XXXX XXXX XXXX XXXX XXXX XXXX XXXX");
        } else {
            account.setName(name);
        }
        account.setOpeningDate(openingDate);
        if (balance < 0) {
            resp.getWriter().println("Нельзя создать счет с отрицательным балансом");
        } else {
            account.setBalance(balance);
        }
        if (user.getId() == 0) {
            resp.getWriter().println("Такого клиента не существует.. Возможно стоит создать сначала клиента :)");
        } else {
            account.setUser(user);
        }
        if (bank.getId() == 0) {
            resp.getWriter().println("Такого банка не существует. Внесите запись о банке. Либо откройте свой и открывайте там счета :).");
        } else {
            account.setBank(bank);
        }
        if (currency.getId() == 0) {
            resp.getWriter().println("Такой валюты нет в справочнике. Сначала дополните справочник валют");
        } else {
            account.setCurrency(currency);
        }
        Account accountAfterSaving = accountService.saveAccount(account);
        resp.getWriter().println(accountAfterSaving);
    }
    /**
     * Реализация метода PUT / Update.
     * Принимает из запроса Value:  id = id банковского счета, (параметр для поиска поля для изменения в БД)
     *                              balance = баланс счета, (изменяемый параметр).
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Account account = new Account();
        account.setId(Long.parseLong(req.getParameter("id")));
        account.setBalance(Double.parseDouble(req.getParameter("balance")));
        Account accountAfterUpdate = accountService.updateAccount(account);
        resp.getWriter().println(accountAfterUpdate);
    }
    /**
     * Реализация метода DELETE.
     * Принимает из запроса Value: id = id банковского счета для удаления записи о нём из БД.
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws IOException - ошибка чтения-записи.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String answer = accountService.deleteAccount(Long.parseLong(req.getParameter("id")));
        resp.getWriter().println(answer);
    }

    /**
     * Метод для проверки правильности введенного пользователем имени банковского счета
     * @param answer принимает номер счета.
     * @return true, если счет правильного формата.
     */
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
