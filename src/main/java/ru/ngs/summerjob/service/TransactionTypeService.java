package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.TransactionType;

public interface TransactionTypeService {
    TransactionType getTransactionTypeById(long id);
    TransactionType saveTransactionType(TransactionType transactionType);
    TransactionType updateTransactionType(TransactionType transactionType);
    String deleteTransactionTypeById(long id);
}
