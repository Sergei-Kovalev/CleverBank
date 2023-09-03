package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Check;

/**
 * @author Sergey Kovalev
 * Интерфейс для взаимодействия с dao.
 * В настоящее время реализован в классах:
 * @see CheckDaoImpl
 */
public interface CheckDao {
    /**
     * Метод для сохранения нового чека.
     * @see Check
     * @param check - объект чека.
     * @return номер чека из БД.
     */
    long saveCheck(Check check);
}
