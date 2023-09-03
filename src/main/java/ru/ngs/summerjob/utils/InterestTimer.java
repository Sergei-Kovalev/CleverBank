package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Bank;
import ru.ngs.summerjob.entity.Transaction;
import ru.ngs.summerjob.service.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Sergey Kovalev
 * Утилитный класс реализующий начисление процентов по счету 31 декабря в 23:59.
 * Является имплементацие интерфейса:
 * @see Runnable
 */
public class InterestTimer implements Runnable {
    /**
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see BankService
     */
    BankService bankService;
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
     * Это поле для загрузки сервиса получающего необходимы данные из БД.
     * @see TransactionService
     */
    TransactionService transactionService;

    /**
     * Конструктор, загружает необходимые имплементации сервисов.
     */
    public InterestTimer() {
        this.bankService = new BankServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.transactionTypeService = new TransactionTypeServiceImpl();
    }

    /**
     * Главный метод сервиса реализующий метод run интерфейса.
     */
    @Override
    public void run() {

        double interest = Double.parseDouble(Config.getConfig().get("bank").get("percentage"));

        LocalDateTime time = LocalDateTime.now();

        if (time.getMonthValue() == 12
                && time.getDayOfMonth() == 31
                && time.getHour() == 23
                && time.getMinute() == 59
                && time.getSecond() >= 30) {

            Bank bank = bankService.getBankByName(Config.getConfig().get("bank").get("name"));

            List<Account> accounts = accountService.getAccountsByBankId(bank.getId());

            for (Account account : accounts) {
                Transaction transaction = new Transaction();
                transaction.setDate(time);
                transaction.setType(transactionTypeService.getTransactionTypeById(4));
                transaction.setAccountSender(null);
                transaction.setAccountRecipient(account);
                int scale = (int) Math.round(account.getBalance() * interest);
                double amount = (double) scale / 100 ;
                transaction.setAmount(amount);
                transactionService.saveTransaction(transaction);
            }
        }
    }
}
