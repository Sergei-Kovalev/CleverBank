package ru.ngs.summerjob.utils;

import ru.ngs.summerjob.config.Config;
import ru.ngs.summerjob.entity.Account;
import ru.ngs.summerjob.entity.Bank;
import ru.ngs.summerjob.entity.Transaction;
import ru.ngs.summerjob.service.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class InterestTimer implements Runnable {
    BankService bankService;
    AccountService accountService;
    TransactionTypeService transactionTypeService;
    TransactionService transactionService;

    public InterestTimer() {
        this.bankService = new BankServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.transactionTypeService = new TransactionTypeServiceImpl();
    }

    @Override
    public void run() {

        double interest = Double.parseDouble(Config.getConfig().get("bank").get("percentage"));

        LocalDateTime time = LocalDateTime.now();

        if (time.getMonthValue() == 8
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
