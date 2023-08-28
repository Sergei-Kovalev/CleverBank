package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.TransactionTypeDAO;
import ru.ngs.summerjob.dao.TransactionTypeDAOImpl;
import ru.ngs.summerjob.entity.TransactionType;

public class TransactionTypeServiceImpl implements TransactionTypeService {

    TransactionTypeDAO transactionTypeDAO;

    public TransactionTypeServiceImpl() {
        this.transactionTypeDAO = new TransactionTypeDAOImpl();
    }

    @Override
    public TransactionType getTransactionTypeById(long id) {
        return transactionTypeDAO.getTransactionTypeById(id);
    }
}
