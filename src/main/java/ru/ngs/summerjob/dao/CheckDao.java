package ru.ngs.summerjob.dao;

import ru.ngs.summerjob.entity.Check;

public interface CheckDao {
    long saveCheck(Check check);
}
