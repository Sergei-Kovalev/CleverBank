package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Bank;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see BankDAOImpl
 */
public interface BankDAO {
    /**
     * Метод для получения банка по id.
     * @see Bank
     * @param id - id банка.
     * @return объект банка.
     */
    Bank getBankById(long id);
    /**
     * Метод для получения банка по имени.
     * @see Bank
     * @param name - имя банка.
     * @return объект банка.
     */
    Bank getBankByName(String name);
    /**
     * Метод для сохранения банка.
     * @see Bank
     * @param bank - объект банка.
     * @return объект сохраненного банка.
     */
    Bank saveBank(Bank bank);
    /**
     * Метод для обновления данных банка.
     * @see Bank
     * @param bank - объект банка.
     * @return объект измененного банка.
     */
    Bank updateBank(Bank bank);
    /**
     * Метод для удаления банка из БД.
     * @see Bank
     * @param id - id банка.
     * @return сообщение об удалении банка.
     */
    String deleteBank(long id);
}
