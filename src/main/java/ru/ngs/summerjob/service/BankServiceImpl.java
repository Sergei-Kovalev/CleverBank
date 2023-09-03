package ru.ngs.summerjob.service;

import ru.ngs.summerjob.dao.BankDAO;
import ru.ngs.summerjob.dao.BankDAOImpl;
import ru.ngs.summerjob.entity.Bank;

/**
 * @author Sergey Kovalev
 * Реализация интерфейса и методов класса:
 * @see BankService
 */
public class BankServiceImpl implements BankService {
    /**
     * Это поле для загрузки dao получающего необходимы данные из БД.
     * @see BankDAO
     */
    BankDAO bankDAO;

    /**
     * Конструктор класса. Загружает необходимые имплементации сервисов.
     */
    public BankServiceImpl() {
        this.bankDAO = new BankDAOImpl();
    }

    /**
     * Имплементация метода возвращающего банк по id.
     * @param id - id банка.
     * @return - объект класса:
     * @see Bank
     */
    @Override
    public Bank getBankById(long id) {
        return bankDAO.getBankById(id);
    }
    /**
     * Имплементация метода возвращающего банк по имени.
     * @param name - name банка.
     * @return - объект класса:
     * @see Bank
     */
    @Override
    public Bank getBankByName(String name) {
        return bankDAO.getBankByName(name);
    }
    /**
     * Имплементация метода сохраняющего объект банка.
     * @see Bank
     * @param bank - принимает объект банка.
     * @return возвращает объект банка сохраненный в базе данных (с заполненным из БД id).
     */
    @Override
    public Bank saveBank(Bank bank) {
        return bankDAO.saveBank(bank);
    }
    /**
     * Имплементация метода обновления данных банка.
     * @see Bank
     * @param bank - принимает объект банка, который необходимо изменить.
     * @return возвращает объект банка измененный в базе дынных (с заполненными из БД данными).
     */
    @Override
    public Bank updateBank(Bank bank) {
        return bankDAO.updateBank(bank);
    }
    /**
     * Имплементация метода удаления банка по id.
     * @param id - параметр принимающий id банка.
     * @return возвращает строку об успешном удалении банка.
     */
    @Override
    public String deleteBank(long id) {
        return bankDAO.deleteBank(id);
    }
}
