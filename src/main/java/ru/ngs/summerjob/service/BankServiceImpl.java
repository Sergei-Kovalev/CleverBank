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
}
