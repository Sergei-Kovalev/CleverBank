package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.BankDAO;
import ru.ngs.summerjob.dao.BankDAOImpl;
import ru.ngs.summerjob.entity.Bank;

public class BankServiceImpl implements BankService {
    BankDAO bankDAO;

    public BankServiceImpl() {
        this.bankDAO = new BankDAOImpl();
    }

    @Override
    public Bank getBankById(long id) {
        return bankDAO.getBankById(id);
    }

    @Override
    public Bank getBankByName(String name) {
        return bankDAO.getBankByName(name);
    }

    @Override
    public Bank saveBank(Bank bank) {
        return bankDAO.saveBank(bank);
    }

    @Override
    public Bank updateBank(Bank bank) {
        return bankDAO.updateBank(bank);
    }

    @Override
    public String deleteBank(long id) {
        return bankDAO.deleteBank(id);
    }
}
