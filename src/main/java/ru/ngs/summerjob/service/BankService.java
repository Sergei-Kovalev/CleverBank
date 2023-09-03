package ru.ngs.summerjob.service;

import ru.ngs.summerjob.entity.Bank;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see BankServiceImpl
 */
public interface BankService {
    /**
     * Метод для получения банка по id.
     * @see Bank
     * @param id - id банка.
     * @return объект банка.
     */
    Bank getBankById(long id);
    /**
     * Метод для получения банка по наименованию.
     * @see Bank
     * @param name - id банка.
     * @return объект банка.
     */
    Bank getBankByName(String name);
    /**
     * Метод для сохранения банка.
     * @param bank - принимает объект банка.
     * @return возвращает сохраненный объект банка.
     * @see Bank
     */
    Bank saveBank(Bank bank);
    /**
     * Метод для обновленя данных банка.
     * @see Bank
     * @param bank - принимает объект банка.
     * @return возвращает измененный объект банка.
     */
    Bank updateBank(Bank bank);
    /**
     * Метод удаления банка по id.
     * @param id - принимает id банка для удаления.
     * @return возвращает строку об успешном удалении.
     */
    String deleteBank(long id);
}
